package zoeyea.skills

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import zoeyea.skills.FleetLogistics.Companion.ACCESS_INCR
import zoeyea.skills.FleetLogistics.Companion.SLIPSTREAM_DETECTION_RADIUS_MULT

class GroundOperations {

    companion object {
        const val GROUND_DEFENSE_INCR = 300f
        const val STABILITY_INCR = 1f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            market.stats.dynamic.getMod(Stats.GROUND_DEFENSES_MOD).modifyFlat(id, GROUND_DEFENSE_INCR, "Ground Operations")
            market.stability.modifyFlat(id, STABILITY_INCR, "Ground Operations")

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            market?.stats?.dynamic?.getMod(Stats.GROUND_DEFENSES_MOD)?.unmodify(id)
            market?.stability?.unmodify(id)

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
                "Increases ground defense rating by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${GROUND_DEFENSE_INCR.toInt()}"
            ).color = Misc.getHighlightColor()

            info.addPara(
                "Increases stability by %s",
                5f,
                Misc.getPositiveHighlightColor(),
                "${STABILITY_INCR.toInt()}x"
            ).color = Misc.getHighlightColor()
        }
    }

}