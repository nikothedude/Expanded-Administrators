package zoeyea.listeners

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.PlayerMarketTransaction
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener
import com.fs.starfarer.api.characters.PersonAPI
import com.fs.starfarer.api.impl.campaign.ids.Skills
import com.fs.starfarer.api.util.WeightedRandomPicker
import zoeyea.MathUtils.prob

class ColonyListener: ColonyInteractionListener {
    companion object {
        const val BASE_UPGRADE_CHANCE = 45f
    }

    override fun reportPlayerOpenedMarket(market: MarketAPI?) {
        if (market == null) return

        /*val admin = market.admin
        if (!admin.isDefault && !admin.memoryWithoutUpdate.getBoolean("\$zea_upgraded")) {
            upgradeAdmin(admin)
            admin.memoryWithoutUpdate["\$zea_upgraded"] = true
        }*/

        for (entry in market.commDirectory.entriesCopy) {
            val person = entry.entryData as? PersonAPI ?: continue
            if (!person.memoryWithoutUpdate.getBoolean("\$ome_isAdmin")) continue
            if (person.memoryWithoutUpdate.getBoolean("\$zea_upgraded")) continue

            var chances = 2
            while (chances-- > 0f) {
                if (prob(BASE_UPGRADE_CHANCE)) {
                    upgradeAdmin(person)
                }
            }
            person.memoryWithoutUpdate["\$zea_upgraded"] = true
        }
    }

    fun upgradeAdmin(admin: PersonAPI) {
        admin.stats.isSkipRefresh = true
        val picker: WeightedRandomPicker<String> = WeightedRandomPicker<String>()
        val allSkillIds = Global.getSettings().sortedSkillIds
        for (skillId in allSkillIds) {
            val skill = Global.getSettings().getSkillSpec(skillId)
            if (admin.stats.hasSkill(skillId)) continue
            if (skill.hasTag(Skills.TAG_DEPRECATED)) continue
            if (skill.hasTag(Skills.TAG_PLAYER_ONLY)) continue
            if (skill.hasTag(Skills.TAG_AI_CORE_ONLY)) continue
            if (skill.isAdminSkill) {
                picker.add(skillId)
            }
        }

        val picked = picker.pick() ?: return
        admin.stats.setSkillLevel(picked, 3f)

        val currTier = admin.memoryWithoutUpdate.getInt("\$ome_adminTier")
        admin.memoryWithoutUpdate["\$ome_adminTier"] = currTier + 1

        admin.stats.isSkipRefresh = false
        admin.stats.refreshCharacterStatsEffects()
    }

    override fun reportPlayerClosedMarket(market: MarketAPI?) {
        return
    }

    override fun reportPlayerOpenedMarketAndCargoUpdated(market: MarketAPI?) {
        return
    }

    override fun reportPlayerMarketTransaction(transaction: PlayerMarketTransaction?) {
        return
    }
}