package zoeyea.skills

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import zoeyea.skills.LuddicFaithful.Companion.ACCESS_INCR
import zoeyea.skills.LuddicFaithful.Companion.GROUND_DEFENSE_INCR
import zoeyea.skills.LuddicFaithful.Companion.STABILITY_INCR

class OrbitalOperations {

    companion object {
        const val PATROL_SPAWN_MULT = 1.5f
        const val FLEET_SIZE_INCR = 0.3f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            market.stats.dynamic.getStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT).modifyFlat(id, PATROL_SPAWN_MULT, "Orbital Operations")
            market.stats.dynamic.getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat(id, FLEET_SIZE_INCR, "Orbital Operations")

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            market?.stats?.dynamic?.getStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT)?.unmodify(id)
            market?.stats?.dynamic?.getMod(Stats.COMBAT_FLEET_SIZE_MULT)?.unmodify(id)

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
                "Increases fleet size by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${(FLEET_SIZE_INCR * 100f).toInt()}%"
            ).color = Misc.getHighlightColor()
            info.addPara(
                "Increases patrol respawn rate by %s",
                5f,
                Misc.getPositiveHighlightColor(),
                "${PATROL_SPAWN_MULT}x"
            )
        }
    }

}