package intensigame.util

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array

internal class ByteArrayLoader : SynchronousAssetLoader<ByteArray, ByteArrayLoader.Parameters>(InternalFileHandleResolver()) {

    override fun load(manager: AssetManager, fileName: String, file: FileHandle, parameter: Parameters?): ByteArray {
        return file.readBytes()
    }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: Parameters?): Array<AssetDescriptor<Any>> {
        return Array()
    }

    internal class Parameters : AssetLoaderParameters<ByteArray>()
}
