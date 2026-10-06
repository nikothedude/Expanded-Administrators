package zoeyea.patches

import com.fs.starfarer.api.characters.PersonAPI
import com.fs.starfarer.api.util.Misc
import patchlib.api.context.AfterContext
import patchlib.api.match.ClassMatch
import patchlib.api.match.MethodMatch
import patchlib.api.patch.After
import patchlib.api.patch.Patch
import zoeyea.skills.LowExpectations

@Patch(target = ClassMatch(type = Misc::class))
object LowExpectationsPatch {
    @JvmStatic
    @After(target = MethodMatch(staticOnly = true, methodName = "getAdminSalary"))
    fun modifyAdminSalary(context: AfterContext) {
        val person = context.getArg(0) as? PersonAPI ?: return
        if (person.stats.hasSkill("zoeyea_low_expectations")) {
            val result = context.returnValue as? Float ?: return
            context.returnValue = result * LowExpectations.COST_MULT
        }
    }

}