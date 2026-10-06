package zoeyea.skills

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import zoeyea.skills.RapidConstruction.Companion.SPEED_MULT

class SafetyProtocols {

    companion object {
        const val HAZARD_REDUCTION = 0.25f
        const val STABILITY_BONUS = 1f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            market.hazard.modifyFlat(id, -HAZARD_REDUCTION, "Safety Protocols")
            market.stability.modifyFlat(id, STABILITY_BONUS, "Safety Protocols")

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            market?.hazard?.unmodify(id)
            market?.stability?.unmodify(id)
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
                "Decreases hazard rating by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${(HAZARD_REDUCTION * 100f).toInt()}%"
            ).color = Misc.getHighlightColor()
            info.addPara(
                "Increases stability by %s",
                5f,
                Misc.getPositiveHighlightColor(),
                "${STABILITY_BONUS.toInt()}"
            ).color = Misc.getHighlightColor()
        }
    }

}