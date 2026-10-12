package zoeyea.skills

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.econ.LuddicMajority
import com.fs.starfarer.api.impl.campaign.ids.Industries
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.impl.campaign.intel.events.LuddicChurchHostileActivityFactor
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import zoeyea.skills.IndustrialZoning.Companion.INDUSTRY_INCR

class LuddicFaithful {

    companion object {
        const val ACCESS_INCR = 0.3f
        const val STABILITY_INCR = 3f
        const val GROUND_DEFENSE_INCR = 50f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            if (LuddicMajority.matchesBonusConditions(market)) {
                market.accessibilityMod.modifyFlat(id, ACCESS_INCR, "Luddic Faithful")
                market.stability.modifyFlat(id, STABILITY_INCR, "Luddic Faithful")
                market.stats.dynamic.getMod(Stats.GROUND_DEFENSES_MOD).modifyFlat(id, GROUND_DEFENSE_INCR, "Luddic Faithful")
            }

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            market?.accessibilityMod?.unmodify(id)
            market?.stability?.unmodify(id)
            market?.stats?.dynamic?.getMod(Stats.GROUND_DEFENSES_MOD)?.unmodify(id)
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
                "If luddic majority is active, has the following effects:",
                0f,
            ).color = Misc.getHighlightColor()

            info.setBulletedListMode(BaseIntelPlugin.BULLET)
            info.addPara(
                "Increases accessibility by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${(ACCESS_INCR * 100f).toInt()}%"
            ).color = Misc.getHighlightColor()
            info.addPara(
                "Increases stability by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${STABILITY_INCR.toInt()}"
            ).color = Misc.getHighlightColor()
            info.addPara(
                "Increases ground defense rating by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${GROUND_DEFENSE_INCR.toInt()}"
            ).color = Misc.getHighlightColor()
            info.setBulletedListMode(null)
        }
    }

}