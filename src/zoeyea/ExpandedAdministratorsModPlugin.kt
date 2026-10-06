package zoeyea

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global
import zoeyea.listeners.ColonyListener

class ExpandedAdministratorsModPlugin : BaseModPlugin() {
    /*This method is run right at the end of starsectors loading.
       * It is most useful for loading data that only really needs to be setup once. */
    @Throws(Exception::class)
    override fun onApplicationLoad() {
        return
    }

    /*This method is run in two cases:
    * - At the end of the creation of a new save
    * - When an existing save finished loading
    * This method is most useful for adding transient listeners/scripts and for enabling mid-save compatibility,
    * like adding star systems to an existing save if the mod was just added. */
    override fun onGameLoad(newGame: Boolean) {
        Global.getSector().listenerManager.addListener(ColonyListener(), true)
    }

    /*Runs when a save is created.
    * This method specifically runs before procedural generation, so any base-game procedural content is not accessible yet.
    * It is recommended to start placing your modded star systems from here,
    * as starsectors procgen will avoid placing stars and hyperspace storms nearby existing systems, preventing overlap.*/
    override fun onNewGame() {
    }

    /*Runs after onNewGame, after the economy has finished loading.
    * This method can be useful for accessing other mods star systems, assuming those have placed their systems in onNewGame. */
    override fun onNewGameAfterEconomyLoad() {
    }

    override fun onNewGameAfterProcGen() {
    }

    override fun onNewGameAfterTimePass() {
    }
}
