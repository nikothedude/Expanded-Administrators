package zoeyea

import org.jetbrains.annotations.Contract
import org.lazywizard.lazylib.MathUtils
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.get
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.text.get

object MathUtils {
    @JvmStatic
    @Contract("null -> 0.0")
    fun ensureIsJsonValidFloat(number: Double?): Double {
        if (number == null || number.isNaN() || number.isInfinite()) {
            //niko_MPC_debugUtils.log.info("ensureIsJsonValidFloat rectifying invalid float $number to 0.0d")
            return 0.0
        }
        return number
    }

    @JvmStatic
    @Contract("null -> 0.0")
    fun Double.ensureIsJsonValidFloat(): Double {
        return ensureIsJsonValidFloat(this)
    }

    @JvmStatic
    @Contract("null -> 0.0")
    fun Float.ensureIsJsonValidFloat(): Double {
        return ensureIsJsonValidFloat(this.toDouble())
    }

    @JvmStatic
    fun Float.roundToMultipleOf(anchor: Float): Float {
        return anchor*((this) / anchor).roundToInt()
    }

    @JvmStatic
    fun Int.roundToMultipleOf(anchor: Float): Float {
        return anchor*((this) / anchor).roundToInt()
    }

    fun Float.trimHangingZero(): Number {
        if (this % 1 == 0f) return this.toInt()
        return this
    }

    fun Double.roundNumTo(decimalPoints: Int): Double {
        var multiplier = 1.0
        repeat(decimalPoints) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    fun Float.roundNumTo(decimalPoints: Int): Float {
        return this.toDouble().roundNumTo(decimalPoints).toFloat()
    }

    fun prob(chance: Int, random: Random = MathUtils.getRandom()): Boolean {
        return prob(chance.toDouble(), random)
    }

    fun prob(chance: Float, random: Random = MathUtils.getRandom()): Boolean {
        return prob(chance.toDouble(), random)
    }

    fun prob(chance: Double, random: Random = MathUtils.getRandom()): Boolean {
        return (random.nextFloat() * 100f < chance)
    }
}