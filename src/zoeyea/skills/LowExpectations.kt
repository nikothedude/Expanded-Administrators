package zoeyea.skills

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import com.fs.starfarer.campaign.CharacterStats
import zoeyea.skills.IndustrialZoning.Companion.INDUSTRY_INCR

class LowExpectations {

    companion object {
        const val COST_MULT = 0.25f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
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
                "Decreases expected salary by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${COST_MULT}x"
            ).color = Misc.getHighlightColor()
        }
    }
}