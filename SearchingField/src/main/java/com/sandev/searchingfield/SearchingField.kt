package com.sandev.searchingfield

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class SearchingField : ConstraintLayout {

    private var listener: SearchingFieldListener? = null
    private lateinit var textEditor: EditText
    private lateinit var ivBack: ImageView
    private lateinit var ivClear: ImageView
    private var previousText: String = ""

    @DrawableRes private var backIcon: Int = 0
    @DrawableRes private var backBackground: Int = 0
    @ColorRes private var backBackgroundColor: Int = 0

    @DrawableRes private var clearIcon: Int = 0
    @DrawableRes private var clearBackground: Int = 0
    @ColorRes private var clearBackgroundColor: Int = 0

    @ColorRes private var editorTextColor: Int = 0
    @Dimension private var editorTextSize: Int = 0

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

    fun setListener(listener: SearchingFieldListener?) {
        this.listener = listener
    }

    fun removeListener() {
        listener = null
    }

    fun setHint(hintText: String) { textEditor.hint = hintText }
    fun setText(textText: String) { textEditor.setText(textText) }
    fun setIconBack(@DrawableRes icon: Int) {
        if (icon != 0) ivBack.setImageDrawable(ContextCompat.getDrawable(context, icon))
    }
    fun setIconBackBackground(@DrawableRes background: Int) {
        if (background != 0) ivBack.background = ContextCompat.getDrawable(context, background)
    }
    fun setIconBackBackgroundColor(@ColorRes color: Int) {
        if (color != 0) ivBack.setBackgroundColor(ContextCompat.getColor(context, color))
    }
    fun setIconClear(@DrawableRes icon: Int) {
        if (icon != 0) ivClear.setImageDrawable(ContextCompat.getDrawable(context, icon))
    }
    fun setIconClearBackground(@DrawableRes background: Int) {
        if (background != 0) ivClear.background = ContextCompat.getDrawable(context, background)
    }
    fun setIconClearBackgroundColor(@ColorRes color: Int) {
        if (color != 0) ivClear.setBackgroundColor(ContextCompat.getColor(context, color))
    }
    fun setEditorTextColor(@ColorRes color: Int) {
        if (color != 0) textEditor.setTextColor(ContextCompat.getColor(context, color))
    }
    fun setEditorTextSize(pix: Int) {
        if (pix != 0) textEditor.textSize = pix.toFloat()
    }

    private fun initializeAttributes(attrs: AttributeSet?) {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.SearchingField, 0, 0)
        backIcon = array.getResourceId(R.styleable.SearchingField_backIcon, R.drawable.ic_back)
        backBackground = array.getResourceId(R.styleable.SearchingField_backIconBackground, 0)
        backBackgroundColor = array.getResourceId(R.styleable.SearchingField_backIconBackgroundColor, 0)

        clearIcon = array.getResourceId(R.styleable.SearchingField_clearIcon, R.drawable.ic_clear)
        clearBackground = array.getResourceId(R.styleable.SearchingField_clearIconBackground, 0)
        clearBackgroundColor = array.getResourceId(R.styleable.SearchingField_clearIconBackgroundColor, 0)

        editorTextColor = array.getResourceId(R.styleable.SearchingField_editorTextColor, 0)
        editorTextSize = array.getLayoutDimension(R.styleable.SearchingField_editorTextSize, 0)
        array.recycle()
    }

    private fun inflateView() {
        val view = View.inflate(context, R.layout.searchfield, this)
        textEditor = view.findViewById(R.id.edText)
        ivBack = view.findViewById(R.id.ivBack)
        ivClear = view.findViewById(R.id.ivClear)

        ivClear.visibility = View.INVISIBLE
        setDecor()
        setupListeners()
    }

    private fun setDecor() {
        setIconBack(backIcon)
        setIconBackBackground(backBackground)
        setIconBackBackgroundColor(backBackgroundColor)

        setIconClear(clearIcon)
        setIconClearBackground(clearBackground)
        setIconClearBackgroundColor(clearBackgroundColor)

        setEditorTextColor(editorTextColor)
        setEditorTextSize(editorTextSize)
    }

    private fun setupListeners() {
        textEditor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                previousText = text?.toString() ?: ""
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listener?.onTextChanged(text.toString(), previousText)
                val size = text?.length ?: 0
                ivClear.visibility = if (size > 0) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        ivClear.setOnClickListener {
            textEditor.text.clear()
            listener?.onClearPressed()
        }

        ivBack.setOnClickListener { listener?.onClosePressed() }
    }

    fun Int.dpToPx(): Int = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics).toInt())
}