package zoeyea.skills

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc

class FleetLogistics {

    companion object {
        const val ACCESS_INCR = 0.3f
        const val SLIPSTREAM_DETECTION_RADIUS_MULT = 2f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            market.accessibilityMod.modifyFlat(id, ACCESS_INCR, "Fleet Logistics")
            market.stats.dynamic.getMod(Stats.SLIPSTREAM_REVEAL_RANGE_LY_MOD)
                .modifyMult(id, SLIPSTREAM_DETECTION_RADIUS_MULT, "Fleet Logistics")

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            market?.accessibilityMod?.unmodify(id)
            market?.stats?.dynamic?.getMod(Stats.SLIPSTREAM_REVEAL_RANGE_LY_MOD)?.unmodify(id)

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
                "Increases accessibility by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${(ACCESS_INCR * 100f).toInt()}%"
            ).color = Misc.getHighlightColor()

            info.addPara(
                "Increases slipstream detection radius by %s",
                5f,
                Misc.getPositiveHighlightColor(),
                "${SLIPSTREAM_DETECTION_RADIUS_MULT.toInt()}x"
            ).color = Misc.getHighlightColor()
        }
    }

}