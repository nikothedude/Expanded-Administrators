package zoeyea.skills

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.CharacterStatsSkillEffect
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.impl.campaign.skills.IndustrialPlanning
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import zoeyea.skills.RapidConstruction.Companion.SPEED_MULT

class RecklessProduction {
    companion object {
        const val HAZARD_INCR = 0.25f
        const val STABILITY_MALUS = 1f
        const val IND_PROD_INCR = 2f
    }

    class LevelOne: BaseSkillEffectDescription(), CharacterStatsSkillEffect, MarketSkillEffect {

        override fun apply(
            stats: MutableCharacterStatsAPI?,
            id: String?,
            level: Float
        ) {
            if (stats == null || id == null) return

            stats.dynamic.getMod(Stats.SUPPLY_BONUS_MOD).modifyFlat(id, IND_PROD_INCR)
        }

        override fun unapply(stats: MutableCharacterStatsAPI?, id: String?) {
            if (stats == null || id == null) return

            stats.dynamic.getMod(Stats.SUPPLY_BONUS_MOD).unmodify(id)
        }

        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null || id == null) return

            market.hazard.modifyFlat(id, HAZARD_INCR, "Profit Maximizing")
            market.stability.modifyFlat(id, -STABILITY_MALUS, "Profit Maximizing")
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            if (market == null || id == null) return
            market.hazard.unmodify(id)
            market.stability.unmodify(id)
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
                "Increases commodity output by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${IND_PROD_INCR.toInt()}"
            ).color = Misc.getHighlightColor()
            info.addPara(
                "Increases hazard rating by %s",
                5f,
                Misc.getNegativeHighlightColor(),
                "${(HAZARD_INCR * 100f).toInt()}%"
            ).color = Misc.getHighlightColor()
            info.addPara(
                "Decreases stability by %s",
                5f,
                Misc.getNegativeHighlightColor(),
                "${STABILITY_MALUS.toInt()}"
            ).color = Misc.getHighlightColor()
        }
    }
}