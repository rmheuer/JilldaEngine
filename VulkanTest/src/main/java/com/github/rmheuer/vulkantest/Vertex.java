package com.github.rmheuer.vulkantest;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.util.SizeOf;
import org.lwjgl.vulkan.VkVertexInputAttributeDescription;
import org.lwjgl.vulkan.VkVertexInputBindingDescription;

import java.nio.ByteBuffer;

import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32B32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_VERTEX_INPUT_RATE_VERTEX;

public class Vertex {
    public static final int SIZEOF = 5 * SizeOf.FLOAT;

    public Vector2f position;
    public Vector3f color;

    public Vertex(Vector2f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public static void getBindingDescription(VkVertexInputBindingDescription bindingDescription) {
        bindingDescription.binding(0);
        bindingDescription.stride(SIZEOF);
        bindingDescription.inputRate(VK_VERTEX_INPUT_RATE_VERTEX);
    }

    public static VkVertexInputAttributeDescription.Buffer getAttributeDescriptions() {
        VkVertexInputAttributeDescription.Buffer buffer = VkVertexInputAttributeDescription.calloc(2);

        VkVertexInputAttributeDescription posDesc = buffer.get(0);
        posDesc.binding(0);
        posDesc.location(0);
        posDesc.format(VK_FORMAT_R32G32_SFLOAT);
        posDesc.offset(0);

        VkVertexInputAttributeDescription colDesc = buffer.get(1);
        colDesc.binding(0);
        colDesc.location(1);
        colDesc.format(VK_FORMAT_R32G32B32_SFLOAT);
        colDesc.offset(2 * SizeOf.FLOAT);

        return buffer;
    }

    public void addToBuffer(ByteBuffer buf) {
        position.put(buf);
        color.put(buf);
    }
}
