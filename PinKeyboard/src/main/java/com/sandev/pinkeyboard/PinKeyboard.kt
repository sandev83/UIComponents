package com.sandev.pinkeyboard

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class PinKeyboard: ConstraintLayout {
    @Dimension
    private var keyWidth: Int = 0
    @Dimension
    private var keyHeight: Int = 0
    @Dimension
    private var keyPadding: Int = 0
    @DrawableRes
    private var pinKeyBakground: Int = 0
    @ColorRes
    private var pinKeyTextColor: Int = 0
    @DrawableRes
    private var leftKeyIcon: Int = 0
    @DrawableRes
    private var leftKeyBackground: Int = 0
    @DrawableRes
    private var rightKeyIcon: Int = 0
    @DrawableRes
    private var rightKeyBackground: Int = 0
    @FontRes
    private var fontFamily: Int = 0

    private var isSorted: Boolean = true

    private lateinit var pinKeys: MutableList<TextView>
    private lateinit var leftKey: ImageView
    private lateinit var rightKey: ImageView
    private lateinit var leftKeyContainer: FrameLayout
    private lateinit var rightKeyContainer: FrameLayout

    private var listener: PinKeyboardListener? = null

    constructor(context: Context) : super(context) {
        inflateView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeAttributes(attrs)
        inflateView()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeAttributes(attrs)
        inflateView()
    }

    fun setListener(listener: PinKeyboardListener?) {
        this.listener = listener
    }

    fun hideLeftKey() {
        leftKeyContainer.visibility = View.INVISIBLE
    }

    fun showLeftKey() {
        leftKeyContainer.visibility = View.VISIBLE
    }

    fun hideRightKey() {
        rightKeyContainer.visibility = View.INVISIBLE
    }

    fun showRightKey() {
        rightKeyContainer.visibility = View.VISIBLE
    }

    fun setPinKeyBackground(@DrawableRes background: Int) {
        for (key in pinKeys) {
            key.background = ContextCompat.getDrawable(context, background)
        }
    }

    fun setPinKeyTextColor(@ColorRes color: Int) {
        for (key in pinKeys) {
            key.setTextColor(ContextCompat.getColorStateList(context, color))
        }
    }

    private fun setLeftKeyIcon(@DrawableRes icon: Int) {
        if (icon != 0) leftKey.setImageResource(icon)
        else leftKey.visibility = View.INVISIBLE
    }

    private fun setRightKeyIcon(@DrawableRes icon: Int) {
        if (icon != 0) rightKey.setImageResource(icon)
        else rightKey.visibility = View.INVISIBLE
    }

    private fun setLeftKeyBackground(@DrawableRes bg: Int) {
        if (bg != 0) leftKeyContainer.background = ContextCompat.getDrawable(context, bg)
    }

    private fun setRightKeyBackground(@DrawableRes bg: Int) {
        if (bg != 0) rightKeyContainer.background = ContextCompat.getDrawable(context, bg)
    }

    private fun setKeyWidth() {
        if (keyWidth == DEFAULT_KEY_WIDTH_DP) return
        for (key in pinKeys) {
            key.layoutParams.width = keyWidth
        }
        leftKey.layoutParams.width = keyWidth
        rightKey.layoutParams.width = keyWidth
        requestLayout()
    }

    private fun setKeyHeight() {
        if (keyHeight == DEFAULT_KEY_HEIGHT_DP) return
        for (key in pinKeys) {
            key.layoutParams.height = keyHeight
        }
        leftKey.layoutParams.height = keyHeight
        rightKey.layoutParams.height = keyHeight
        requestLayout()
    }

    private fun setKeyPadding() {
        for (key in pinKeys) {
            key.setPadding(keyPadding, keyPadding, keyPadding, keyPadding)
            key.compoundDrawablePadding = -1 * keyPadding
        }
        leftKey.setPadding(keyPadding, keyPadding, keyPadding, keyPadding)
        rightKey.setPadding(keyPadding, keyPadding, keyPadding, keyPadding)
    }

    private fun setPinKeyFontFamily() {
        if (fontFamily != 0) {
            val typeface = ResourcesCompat.getFont(context, fontFamily)
            for (key in pinKeys) {
                key.typeface = typeface
            }
        }
    }

    private fun initializeAttributes(attrs: AttributeSet?) {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.PinKeyboard, 0, 0)
        try {
            val type = array.getInt(R.styleable.PinKeyboard_pinKeyboardType, -1)
            if (type == -1) throw IllegalArgumentException("keyboardType attribute is required.")
            keyWidth = array.getLayoutDimension(R.styleable.PinKeyboard_pinKeyboardWidth, DEFAULT_KEY_WIDTH_DP)
            keyHeight = array.getLayoutDimension(R.styleable.PinKeyboard_pinKeyboardHeight, DEFAULT_KEY_HEIGHT_DP)
            keyPadding = array.getDimensionPixelSize(R.styleable.PinKeyboard_pinKeyboardPadding, dpToPx(DEFAULT_KEY_PADDING_DP.toFloat()))
            pinKeyBakground = array.getResourceId(R.styleable.PinKeyboard_pinKeyboardBackground,0)
            pinKeyTextColor = array.getResourceId(R.styleable.PinKeyboard_pinKeyboardTextColor, 0)
            rightKeyIcon = array.getResourceId(R.styleable.PinKeyboard_pinKeyboardRightButtonIcon, 0)
            rightKeyBackground = array.getResourceId(R.styleable.PinKeyboard_pinKeyboardRightButtonBackground, 0)
            fontFamily = array.getResourceId(R.styleable.PinKeyboard_pinKeyboardTextFontFamily, 0)
            isSorted = array.getBoolean(R.styleable.PinKeyboard_pinKeyboardSortedKeys, true)

            when (type) {
                0 -> { // integer
                    leftKeyIcon = 0
                    leftKeyBackground = 0
                }
                1 -> { // fingerprint
                    leftKeyIcon = array.getResourceId(R.styleable.PinKeyboard_pinKeyboardLeftButtonIcon, 0)
                    leftKeyBackground = array.getResourceId(R.styleable.PinKeyboard_pinKeyboardLeftButtonBackground, 0)
                }
                else -> {
                    leftKeyIcon = 0
                    leftKeyBackground = 0
                }
            }
        } finally {
            array.recycle()
        }
    }

    private fun inflateView() {
        val view = View.inflate(context, R.layout.pin_keyboard, this)
        pinKeys = ArrayList(10)
        pinKeys.add(view.findViewById(R.id.key1))
        pinKeys.add(view.findViewById(R.id.key2))
        pinKeys.add(view.findViewById(R.id.key3))
        pinKeys.add(view.findViewById(R.id.key4))
        pinKeys.add(view.findViewById(R.id.key5))
        pinKeys.add(view.findViewById(R.id.key6))
        pinKeys.add(view.findViewById(R.id.key7))
        pinKeys.add(view.findViewById(R.id.key8))
        pinKeys.add(view.findViewById(R.id.key9))
        pinKeys.add(view.findViewById(R.id.key0))
        leftKey = view.findViewById(R.id.leftKey)
        rightKey = view.findViewById(R.id.rightKey)
        leftKeyContainer = view.findViewById(R.id.leftKeyBkg)
        rightKeyContainer = view.findViewById(R.id.rightKeyBkg)
        fillNumbers()
        setStyles()
        setupListeners()
    }

    private fun fillNumbers() {
        if (isSorted) {
            pinKeys.forEach {
                val index = pinKeys.indexOf(it)
                if (index != 9) it.text = "${index + 1}"
                else it.text = "0"
            }
        } else {
            val list: MutableList<Int> = ArrayList()
            val rnd = Random(System.currentTimeMillis())
            for (i in 0..9) {
                var n = 0
                do {
                    n = rnd.nextInt(10)
                } while (list.contains(n))
                list.add(n)
            }
            pinKeys.forEach {
                it.text = "${list[pinKeys.indexOf(it)]}"
            }
        }
    }

    private fun setStyles() {
        setKeyWidth()
        setKeyHeight()
        setKeyPadding()
        setPinKeyBackground(pinKeyBakground)
        setPinKeyTextColor(pinKeyTextColor)
        setPinKeyFontFamily()
        setLeftKeyIcon(leftKeyIcon)
        setLeftKeyBackground(leftKeyBackground)
        setRightKeyIcon(rightKeyIcon)
        setRightKeyBackground(rightKeyBackground)
    }

    private fun setupListeners() {
        for (i in pinKeys.indices) {
            val key = pinKeys[i]
            key.setOnClickListener {
                listener?.onPinKeyClicked(key.text.toString().toInt())
            }
        }
        leftKey.setOnClickListener {
            listener?.onLeftButtonClicked()
        }
        rightKey.setOnClickListener {
            listener?.onRightButtonClicked()
        }
    }

    fun dpToPx(valueInDp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, resources.displayMetrics).toInt()
    }

    companion object {

        private const val DEFAULT_KEY_WIDTH_DP = -1 // match_parent
        private const val DEFAULT_KEY_HEIGHT_DP = -1 // match_parent
        private const val DEFAULT_KEY_PADDING_DP = 8

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}