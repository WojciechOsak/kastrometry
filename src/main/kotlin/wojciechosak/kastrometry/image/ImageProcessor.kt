package wojciechosak.kastrometry.image

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.filter.ThresholdFilter
import com.sksamuel.scrimage.nio.JpegWriter
import java.io.File

class ImageProcessor(
    imagePath: String,
    outputPath: String = "output.jpg",
    threshold: Int = 75
) {

    init {
        loadImage(imagePath).apply {
            filter(ThresholdFilter(threshold)).also { image ->
                val writer = JpegWriter().withProgressive(true)


                image.output(writer, File(outputPath))
                println(image)
            }
        }

    }

    private fun loadImage(path: String): ImmutableImage {
        val inputStream = ImageProcessor::class.java.getResourceAsStream(path)
        return ImmutableImage.loader().fromStream(inputStream)!!
    }
}
