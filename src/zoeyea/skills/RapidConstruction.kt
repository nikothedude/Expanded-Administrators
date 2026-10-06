package zoeyea.skills

import com.fs.starfarer.api.campaign.SectorEntityToken
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import zoeyea.BaseZoeyScript
import zoeyea.skills.QualityControl.Companion.QUALITY_BONUS

class RapidConstruction {

    companion object {
        const val SPEED_MULT = 2f

        fun addScript(market: MarketAPI) {
            if (market.primaryEntity == null) return
            if (market.primaryEntity.hasScriptOfClass(RapidConstructionScript::class.java)) return

            RapidConstructionScript(market.primaryEntity).start()
        }

        fun removeScript(market: MarketAPI) {
            if (market.primaryEntity == null) return
            val script = market.primaryEntity.scripts.find { it is RapidConstructionScript } as? RapidConstructionScript ?: return
            script.delete()
        }

        class RapidConstructionScript(val entity: SectorEntityToken): BaseZoeyScript() {
            override fun startImpl() {
                entity.addScript(this)
            }

            override fun stopImpl() {
                entity.removeScript(this)
            }

            override fun runWhilePaused(): Boolean = false

            override fun advance(amount: Float) {
                val market = entity.market
                if (market == null) {
                    delete()
                    return
                }
                if (!market.admin.stats.hasSkill("zea_rapid_construction")) {
                    delete()
                    return
                }
                val days = Misc.getDays(amount)
                for (iterInd in market.industries) {
                    iterInd as? BaseIndustry ?: continue
                    if (!iterInd.isBuilding && !iterInd.isUpgrading) continue

                    iterInd.buildProgress += days * (SPEED_MULT / 2f)
                }
            }
        }
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {
        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            if (id == null) return

            addScript(market)

            return
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            if (market == null) return
            removeScript(market)

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
                "Speeds up construction and upgrading by %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${SPEED_MULT.toInt()}x"
            ).color = Misc.getHighlightColor()
        }
    }

}