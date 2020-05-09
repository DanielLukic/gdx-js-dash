package intensigame.graphics

import com.badlogic.gdx.graphics.g2d.TextureRegion

class ImageRegion(
    val data: TextureRegion
) {
    val width = data.regionWidth
    val height = data.regionHeight
}
