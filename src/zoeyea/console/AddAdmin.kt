package zoeyea.console

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent
import com.fs.starfarer.api.impl.campaign.ids.Factions
import org.lazywizard.console.BaseCommand
import org.lazywizard.console.Console
import zoeyea.MathUtils

class AddAdmin: BaseCommand {
    override fun runCommand(
        param: String,
        context: BaseCommand.CommandContext
    ): BaseCommand.CommandResult {

        val input = if (param.isNotEmpty()) param[0].digitToIntOrNull() ?: org.lazywizard.lazylib.MathUtils.getRandomNumberInRange(0, 5) else null
        val level = input ?: org.lazywizard.lazylib.MathUtils.getRandomNumberInRange(0, 5)
        if (level !in 0..5) {
            Console.showMessage("Level cannot exceed 5 or be below 0!")
            return BaseCommand.CommandResult.ERROR
        }

        val admin = OfficerManagerEvent.createAdmin(Global.getSector().playerFaction, level, org.lazywizard.lazylib.MathUtils.getRandom())
        var skillString = ""
        for (skill in admin.stats.skillsCopy) {
            if (!skill.skill.isAdminSkill) continue
            skillString += "${skill.skill.name}, "
        }
        Console.showMessage(
            "Added admin ${admin.name.fullName}, with skills: $skillString"
        )

        Global.getSector().characterData.addAdmin(admin)

        return BaseCommand.CommandResult.SUCCESS
    }
}