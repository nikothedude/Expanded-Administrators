package zoeyea.skills

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import com.fs.starfarer.api.campaign.FleetInflater
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.listeners.FleetInflationListener
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.fleets.DefaultFleetInflaterParams
import com.fs.starfarer.api.impl.campaign.ids.Commodities
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import org.magiclib.kotlin.getSourceMarket
import org.magiclib.kotlin.isPatrol
import org.magiclib.kotlin.isWarFleet
import zoeyea.skills.OrbitalOperations.Companion.FLEET_SIZE_INCR
import zoeyea.skills.OrbitalOperations.Companion.PATROL_SPAWN_MULT

class QualityControl {

    companion object {
        const val SMOD_INCR = 1
        const val QUALITY_BONUS = 0.3f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            QualityControlInflationListener.add(market)
            market.stats.dynamic.getMod(Stats.FLEET_QUALITY_MOD).modifyFlat(id, QUALITY_BONUS, "Quality Control")

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            if (market == null) return

            market.stats.dynamic.getMod(Stats.FLEET_QUALITY_MOD).unmodify(id)

            return
        }

        override fun createCustomDescription(
            stats: MutableCharacterStatsAPI?,
            skill: SkillSpecAPI?,
            info: TooltipMakerAPI?,
            width: Float
        ) {
            super.createCustomDescription(stats, skill, info, width)

            if (info == null) return

            info.addPara(
                "Adds %s to fleets launched by this colony",
                0f,
                Misc.getStoryOptionColor(),
                "s-mods"
            ).color = Misc.getHighlightColor()
            info.addPara(
                "Increases quality of fleets launched by this colony by %s",
                5f,
                Misc.getPositiveHighlightColor(),
                "${(QUALITY_BONUS * 100f).toInt()}%"
            ).color = Misc.getHighlightColor()
        }
    }

    class QualityControlInflationListener(): FleetInflationListener {
        val markets = HashSet<MarketAPI>()

        companion object {
            fun add(market: MarketAPI) {
                get().markets += market
            }

            fun init() {
                if (!Global.getSector().listenerManager.hasListenerOfClass(QualityControlInflationListener::class.java)) {
                    Global.getSector().listenerManager.addListener(QualityControlInflationListener(), false)
                }
            }

            fun get(): QualityControlInflationListener {
                init()
                return Global.getSector().listenerManager.getListeners(QualityControlInflationListener::class.java).first()
            }
        }

        override fun reportFleetInflated(
            fleet: CampaignFleetAPI?,
            inflater: FleetInflater?
        ) {
            markets.retainAll { it.size >= 3f && it.admin?.stats?.hasSkill("zea_quality_control") == true }

            // UNHOLY CODE
            if (fleet == null || inflater == null) return
            if (fleet.getSourceMarket() !in markets) return
            if (fleet.memoryWithoutUpdate["\$zea_quality_control_InflatedAlready"] == true) return
            val params = inflater.params as? DefaultFleetInflaterParams ?: return

            if (fleet.isStationMode) return

            val orig = params.averageSMods ?: 0
            params.averageSMods = (orig + SMOD_INCR)

            fleet.memoryWithoutUpdate.set("\$zea_quality_control_InflatedAlready", true)
            fleet.deflate()
            val oldRemove = inflater.removeAfterInflating()
            inflater.setRemoveAfterInflating(false)
            fleet.inflateIfNeeded()
            inflater.setRemoveAfterInflating(oldRemove)
        }
    }

}