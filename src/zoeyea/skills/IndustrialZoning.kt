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

class IndustrialZoning {

    companion object {
        const val INDUSTRY_INCR = 1f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            market.stats.dynamic.getMod(Stats.MAX_INDUSTRIES).modifyFlat(id, INDUSTRY_INCR, "Industrial Zoning")

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            market?.stats?.dynamic?.getMod(Stats.MAX_INDUSTRIES)?.unmodify(id)

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
                "Increases maximum industries by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${INDUSTRY_INCR.toInt()}"
            ).color = Misc.getHighlightColor()
        }
    }
}