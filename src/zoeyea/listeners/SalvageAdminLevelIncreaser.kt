package zoeyea.listeners

import com.fs.starfarer.api.campaign.BaseCampaignEventListener
import com.fs.starfarer.api.campaign.InteractionDialogAPI
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.SleeperPodsSpecial
import org.magiclib.kotlin.getSalvageSpecial
import zoeyea.MathUtils.prob
import zoeyea.listeners.ColonyListener.Companion.BASE_UPGRADE_CHANCE

class SalvageAdminLevelIncreaser: BaseCampaignEventListener(false) {
    override fun reportShownInteractionDialog(dialog: InteractionDialogAPI?) {
        super.reportShownInteractionDialog(dialog)

        if (dialog == null) return

        val entity = dialog.interactionTarget ?: return

        val special = entity.getSalvageSpecial() as? SleeperPodsSpecial.SleeperPodsSpecialData ?: return
        if (special.officer == null || !special.officer.memoryWithoutUpdate.getBoolean("\$ome_isAdmin")) return
        if (special.officer.memoryWithoutUpdate.getBoolean("\$zea_upgraded")) return

        var chances = 3
        while (chances-- > 0f) {
            if (prob(BASE_UPGRADE_CHANCE)) {
                ColonyListener.upgradeAdmin(special.officer)
            }
        }
        special.officer.memoryWithoutUpdate["\$zea_upgraded"] = true
    }
}