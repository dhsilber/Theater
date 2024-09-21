package display

enum class DrawingOrderOperation {
  CIRCLE,  // Circle data is: x, y, r
  LINE,  // Line data is: x1, y1, x2, y2
  THICK_LINE,
  DASHED_LINE, // Line data is: x1, y1, x2, y2
  RECTANGLE, // Rectangle data is: x, y, width, height
  FILLED_RECTANGLE, // Rectangle data is: x, y, width, height
  USE, // Use data is: x, y. useType must be provided.
  FILLED_RIGHT_TRIANGLE, // Triangle data is: three sets of x,y coordinates
}
