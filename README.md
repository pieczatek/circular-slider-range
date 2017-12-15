Circular Slider Range
=====================

Forked from https://github.com/bozapro/circular-slider-range
All glory to @bozapro

Sample usage
------------
    ...
    <com.bozapro.circularsliderrange.CircularSliderRange
        android:id="@+id/circular"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="30dp"
        range:arc_color="@color/colorAccent"
        range:arc_dash_size="20dp"
        range:border_color="#505090"
        range:border_thickness="14dp"
        range:end_angle="30"
        range:end_thumb_image="@drawable/circle_shape"
        range:start_angle="120"
        range:start_thumb_color="#30AEFF"
        range:start_thumb_size="60dp"
        range:thumb_size="50dp" />
    ...

Explanation of attributes
-------------------------

- `arc_color`: (**refference**) Color of the arc drawn between two thumbs.
- `arc_dash_size`: (**dimension**) Define dash size of the arc drawn between two thumbs.
- `start_angle`: (**float**) The position of the slider start thumb, a degrees based value of the angle (0-360).
- `end_angle`: (**float**) The position of the slider end thumb, a degrees based value of the angle (0-360).
- `border_thickness`: (**dimension**) How thick should the slider border be (this can be a 0-dimension).
- `border_color`: (**color**) Recolors the slider border to the specified color.
- `thumb_size`: (**dimension**) Radius of the slider thumb (thumb is the slider's movable part). May be overridden with specific values for start/end thumbs.
- `end_thumb_size`: (**dimension**) Radius of the end slider thumb (thumb is the slider's movable part). This value will override `thumb_size` for end thumb.
- `start_thumb_size`: (**dimension**) Radius of the start slider thumb (thumb is the slider's movable part). This value will override `thumb_size` for start thumb.
- `start_thumb_image`: (**reference**) Set this to use an image instead of a colored circle for the slider start thumb.
- `end_thumb_image`: (**reference**) Set this to use an image instead of a colored circle for the slider end thumb.
- `start_thumb_color`: (**color**) Set this to use a colored circle instead of an image for the slider start thumb.
- `end_thumb_color`: (**color**) Set this to use a colored circle instead of an image for the slider end thumb.

