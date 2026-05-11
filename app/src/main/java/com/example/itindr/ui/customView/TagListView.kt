package com.example.itindr.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.itindr.R
import com.example.itindr.util.shake
import com.example.itindr.util.vibrate
import kotlin.math.min

fun View.dpToPx(dp: Float): Float = dp * resources.displayMetrics.density

fun View.spToPx(sp: Float): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)

private const val DEFAULT_TEXT_SIZE = 14f
private const val CORNER_RADIUS = 16f
private const val HORIZONTAL_PADDING = 12f
private const val VERTICAL_PADDING = 4f
private const val SPACING = 8f
private const val VIBRATION_DURATION = 50L
private const val DELTA = 5f
private const val SHAKE_DURATION = 300L

@Suppress("unused")
class TagListView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    data class Tag(val id: String, val text: String)
    enum class SelectionMode { SINGLE, MULTI }

    private var tags: List<Tag> = emptyList()
    private val selectedIds = mutableSetOf<String>()
    private var mode = SelectionMode.MULTI
    private var maxSelected: Int? = null

    private var onSelectionLimitReached: ((Int) -> Unit)? = null
    private var onSelectionChange: ((Set<String>) -> Unit)? = null
    private var onTagClick: ((String, Boolean) -> Unit)? = null

    private val tagBounds = mutableListOf<RectF>()
    private var pressedTagIndex = -1

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val selectedBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var normalTextColor = context.getColor(R.color.white)
    private var selectedTextColor = context.getColor(R.color.black)

    private var cornerRadius = 0f
    private var tagHorizontalPadding = 0f
    private var tagVerticalPadding = 0f
    private var tagSpacingHorizontal = 0f
    private var tagSpacingVertical = 0f

    private var fixedTagWidth = 0f
    private var fixedTagHeight = 0f

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TagListView, defStyleAttr, 0).apply {
            try {
                val modeIndex = getInt(R.styleable.TagListView_selectionMode, 0)
                mode = if (modeIndex == 0) SelectionMode.MULTI else SelectionMode.SINGLE

                maxSelected = getInteger(R.styleable.TagListView_maxSelected, -1).takeIf { it > 0 }

                backgroundPaint.color = getColor(R.styleable.TagListView_tagNormalBackground,
                    context.getColor(R.color.white_transparent))
                selectedBackgroundPaint.color = getColor(R.styleable.TagListView_tagSelectedBackground,
                    Color.WHITE)

                normalTextColor = getColor(R.styleable.TagListView_tagNormalTextColor,
                    context.getColor(R.color.white))
                selectedTextColor = getColor(R.styleable.TagListView_tagSelectedTextColor,
                    context.getColor(R.color.black))

                val defaultTextSize = spToPx(DEFAULT_TEXT_SIZE)
                textPaint.textSize = getDimension(R.styleable.TagListView_tagTextSize, defaultTextSize)

                val fontResourceId = getResourceId(R.styleable.TagListView_tagTextFontFamily, 0)
                if (fontResourceId != 0) {
                    try {
                        val typeface = ResourcesCompat.getFont(context, fontResourceId)
                        textPaint.typeface = typeface
                    } catch (_: Exception) {
                    }
                }

                cornerRadius = getDimension(R.styleable.TagListView_tagCornerRadius, dpToPx(
                    CORNER_RADIUS))

                tagHorizontalPadding = getDimension(R.styleable.TagListView_tagHorizontalPadding, dpToPx(HORIZONTAL_PADDING))
                tagVerticalPadding = getDimension(R.styleable.TagListView_tagVerticalPadding, dpToPx(VERTICAL_PADDING))
                tagSpacingHorizontal = getDimension(R.styleable.TagListView_tagSpacingHorizontal, dpToPx(SPACING))
                tagSpacingVertical = getDimension(R.styleable.TagListView_tagSpacingVertical, dpToPx(SPACING))

                fixedTagWidth = getDimension(R.styleable.TagListView_tagWidth, 0f)
                fixedTagHeight = getDimension(R.styleable.TagListView_tagHeight, 0f)

                val entriesResId = getResourceId(R.styleable.TagListView_tagEntries, 0)
                if (entriesResId != 0) {
                    try {
                        val stringArray = resources.getStringArray(entriesResId)
                        tags = stringArray.mapIndexed { index, text -> Tag(index.toString(), text) }
                        requestLayout()
                        invalidate()
                    } catch (_: Exception) {
                    }
                }
            } finally {
                recycle()
            }
        }
    }

    fun setTags(newTags: List<Tag>) {
        tags = newTags
        selectedIds.retainAll(newTags.map { it.id }.toSet())
        requestLayout()
        invalidate()
    }

    fun setMode(mode: SelectionMode) {
        this.mode = mode
        if (mode == SelectionMode.SINGLE && selectedIds.size > 1) {
            val first = selectedIds.first()
            selectedIds.clear()
            selectedIds.add(first)
            onSelectionChange?.invoke(selectedIds)
        }
        invalidate()
    }

    fun setMaxSelected(limit: Int?) {
        maxSelected = limit
        if (limit != null && selectedIds.size > limit) {
            val orderedSelected = tags.map { it.id }.filter { selectedIds.contains(it) }
            val newSelected = orderedSelected.take(limit).toSet()
            selectedIds.clear()
            selectedIds.addAll(newSelected)
            onSelectionChange?.invoke(selectedIds)
        }
        invalidate()
    }

    fun setSelectedIds(ids: Set<String>) {
        selectedIds.clear()
        selectedIds.addAll(ids.intersect(tags.map { it.id }.toSet()))
        onSelectionChange?.invoke(selectedIds)
        invalidate()
    }

    fun getSelectedIds(): Set<String> = selectedIds.toSet()

    fun setOnSelectionChangeListener(listener: (Set<String>) -> Unit) {
        onSelectionChange = listener
    }

    fun setOnTagClickListener(listener: (String, Boolean) -> Unit) {
        onTagClick = listener
    }

    fun setOnSelectionLimitReachedListener(listener: (Int) -> Unit) {
        onSelectionLimitReached = listener
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val availableWidth = width - paddingLeft - paddingRight

        if (tags.isEmpty()) {
            setMeasuredDimension(width, paddingTop + paddingBottom)
            return
        }

        tagBounds.clear()

        var currentX = paddingLeft.toFloat()
        var currentY = paddingTop.toFloat()
        var lineHeight = 0f

        for (tag in tags) {
            val tagWidth = if (fixedTagWidth > 0) {
                fixedTagWidth
            } else {
                val textWidth = textPaint.measureText(tag.text)
                textWidth + 2 * tagHorizontalPadding
            }

            val tagHeight = if (fixedTagHeight > 0) {
                fixedTagHeight
            } else {
                getDynamicTagHeight()
            }

            if (currentX + tagWidth > paddingLeft + availableWidth && currentX > paddingLeft) {
                currentX = paddingLeft.toFloat()
                currentY += lineHeight + tagSpacingVertical
                lineHeight = 0f
            }

            tagBounds.add(RectF(currentX, currentY, currentX + tagWidth, currentY + tagHeight))

            if (tagHeight > lineHeight) lineHeight = tagHeight

            currentX += tagWidth + tagSpacingHorizontal
        }

        val totalHeight = currentY + lineHeight + paddingBottom

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> height
            MeasureSpec.AT_MOST -> min(totalHeight.toInt(), height)
            else -> totalHeight.toInt()
        }

        setMeasuredDimension(width, finalHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (tags.isEmpty()) return

        for (i in tags.indices) {
            val tag = tags[i]
            val rect = tagBounds[i]
            val isSelected = selectedIds.contains(tag.id)

            textPaint.color = if (isSelected) selectedTextColor else normalTextColor

            val background = if (isSelected) selectedBackgroundPaint else backgroundPaint
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, background)

            val baseline = rect.top +
                (rect.height() - (textPaint.descent() - textPaint.ascent())) / 2 - textPaint.ascent()
            canvas.drawText(tag.text, rect.left + tagHorizontalPadding, baseline, textPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pressedTagIndex = findTagIndex(event.x, event.y)
                return pressedTagIndex != -1
            }
            MotionEvent.ACTION_UP -> {
                if (pressedTagIndex != -1 && pressedTagIndex == findTagIndex(event.x, event.y)) {
                    handleTagClick(pressedTagIndex)
                }
                pressedTagIndex = -1
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                pressedTagIndex = -1
            }
        }
        return super.onTouchEvent(event)
    }

    private fun findTagIndex(x: Float, y: Float): Int {
        for (i in tagBounds.indices) {
            if (tagBounds[i].contains(x, y)) return i
        }
        return -1
    }

    private fun handleTagClick(index: Int) {
        val tag = tags[index]
        val id = tag.id
        val wasSelected = selectedIds.contains(id)

        when (mode) {
            SelectionMode.SINGLE -> handleSingleClick(id, wasSelected)
            SelectionMode.MULTI -> handleMultiClick(id, wasSelected)
        }

        val isSelectedAfter = selectedIds.contains(id)
        onTagClick?.invoke(id, isSelectedAfter)
        onSelectionChange?.invoke(selectedIds)
        invalidate()
    }

    private fun handleSingleClick(id: String, wasSelected: Boolean) {
        selectedIds.clear()
        if (!wasSelected) {
            selectedIds.add(id)
        }
    }

    private fun handleMultiClick(id: String, wasSelected: Boolean) {
        if (wasSelected) {
            selectedIds.remove(id)
            return
        }

        maxSelected?.let { limit ->
            if (selectedIds.size >= limit) {
                vibrate(context, VIBRATION_DURATION)
                shake(dpToPx(DELTA), SHAKE_DURATION)
                onSelectionLimitReached?.invoke(limit)
                return
            }
        }
        selectedIds.add(id)
    }

    private fun getDynamicTagHeight(): Float {
        return textPaint.descent() - textPaint.ascent() + 2 * tagVerticalPadding
    }
}
