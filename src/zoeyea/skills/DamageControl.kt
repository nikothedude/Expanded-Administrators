package zoeyea.skills

import com.fs.starfarer.api.campaign.SectorEntityToken
import com.fs.starfarer.api.campaign.econ.Industry
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.characters.MarketSkillEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.econ.LuddicMajority
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.impl.campaign.skills.BaseSkillEffectDescription
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import zoeyea.BaseZoeyScript
import zoeyea.skills.LuddicFaithful.Companion.ACCESS_INCR
import zoeyea.skills.LuddicFaithful.Companion.GROUND_DEFENSE_INCR
import zoeyea.skills.LuddicFaithful.Companion.STABILITY_INCR
import zoeyea.skills.RapidConstruction.Companion.RapidConstructionScript
import zoeyea.skills.RapidConstruction.Companion.SPEED_MULT

class DamageControl {

    companion object {
        const val MAX_DAYS = 60f
        const val GROUND_DEF_MULT = 1.2f
    }

    class LevelOne: BaseSkillEffectDescription(), MarketSkillEffect {

        class DamageControlScript(val entity: SectorEntityToken): BaseZoeyScript() {
            val allowedInds = HashSet<String>()

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
                if (!market.admin.stats.hasSkill("zoeyea_damage_control")) {
                    delete()
                    return
                }
                val days = Misc.getDays(amount)
                allowedInds.retainAll { market.hasIndustry(it) }
                for (iterInd in market.industries) {
                    iterInd as? BaseIndustry ?: continue
                    if (iterInd.spec.id !in allowedInds) {
                        if (iterInd.disruptedDays > MAX_DAYS) continue
                        allowedInds.add(iterInd.spec.id)
                    }
                    if (iterInd.spec.id in allowedInds && iterInd.disruptedDays > MAX_DAYS) {
                        iterInd.setDisrupted(MAX_DAYS)
                        // check each ind once on first iter? if disrupted, skip til its not?
                    }

                    iterInd.buildProgress += days * (SPEED_MULT / 2f)
                }
            }
        }

        override fun apply(
            market: MarketAPI?,
            id: String?,
            level: Float
        ) {
            if (market == null) return
            market.stats.dynamic.getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(id, GROUND_DEF_MULT, "Damage Control")

            if (market.primaryEntity == null) return
            if (market.primaryEntity.hasScriptOfClass(DamageControlScript::class.java)) return

            DamageControlScript(market.primaryEntity).start()
        }

        override fun unapply(market: MarketAPI?, id: String?) {
            if (market == null) return

            market.stats.dynamic.getMod(Stats.GROUND_DEFENSES_MOD).unmodify(id)

            if (market.primaryEntity == null) return
            val script = market.primaryEntity.scripts.find { it is DamageControlScript } as? DamageControlScript ?: return
            script.delete()
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
                "Structures cannot be disrupted for more than %s",
                0f,
                Misc.getPositiveHighlightColor(),
                "${MAX_DAYS.toInt()} days"
            ).color = Misc.getHighlightColor()

            info.addPara(
                "+%s ground defenses",
                5f,
                Misc.getPositiveHighlightColor(),
                "${GROUND_DEF_MULT}x"
            ).color = Misc.getHighlightColor()
        }
    }
}