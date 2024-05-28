package cytech.morphing;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class GifSequenceWriter {
  public ImageWriter gifWriter;
  public ImageWriteParam imageWriteParam;
  public IIOMetadata imageMetaData;

  public GifSequenceWriter(
      ImageOutputStream outputStream,
      int imageType,
      int timeBetweenFramesMS,
      boolean loopContinuously) throws IIOException, IOException {
    gifWriter = getWriter();
    imageWriteParam = gifWriter.getDefaultWriteParam();
    ImageTypeSpecifier imageTypeSpecifier =
      ImageTypeSpecifier.createFromBufferedImageType(imageType);

    imageMetaData =
      gifWriter.getDefaultImageMetadata(imageTypeSpecifier,
      imageWriteParam);

    String metaFormatName = imageMetaData.getNativeMetadataFormatName();

    IIOMetadataNode root = (IIOMetadataNode)
      imageMetaData.getAsTree(metaFormatName);

    IIOMetadataNode graphicsControlExtensionNode = getNode(
      root,
      "GraphicControlExtension");

    graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
    graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
    graphicsControlExtensionNode.setAttribute(
      "transparentColorFlag",
      "FALSE");
    graphicsControlExtensionNode.setAttribute(
      "delayTime",
      Integer.toString(timeBetweenFramesMS / 10));
    graphicsControlExtensionNode.setAttribute(
      "transparentColorIndex",
      "0");

    IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
    commentsNode.setAttribute("CommentExtension", "Created by MAH");

    IIOMetadataNode appEntensionsNode = getNode(
      root,
      "ApplicationExtensions");

    IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

    child.setAttribute("applicationID", "NETSCAPE");
    child.setAttribute("authenticationCode", "2.0");

    int loop = loopContinuously ? 0 : 1;

    child.setUserObject(new byte[]{ 0x1, (byte) (loop & 0xFF), (byte)
      ((loop >> 8) & 0xFF)});
    appEntensionsNode.appendChild(child);

    imageMetaData.setFromTree(metaFormatName, root);

    gifWriter.setOutput(outputStream);

    gifWriter.prepareWriteSequence(null);
  }

  public void writeToSequence(RenderedImage img) throws IOException {
    gifWriter.writeToSequence(
      new IIOImage(
        img,
        null,
        imageMetaData),
      imageWriteParam);
  }

  public void close() throws IOException {
    gifWriter.endWriteSequence();
  }

  private static ImageWriter getWriter() throws IIOException {
    Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
    if(!iter.hasNext()) {
      throw new IIOException("No GIF Image Writers Exist");
    } else {
      return iter.next();
    }
  }

  private static IIOMetadataNode getNode(
      IIOMetadataNode rootNode,
      String nodeName) {
    int nNodes = rootNode.getLength();
    for (int i = 0; i < nNodes; i++) {
      if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)
          == 0) {
        return((IIOMetadataNode) rootNode.item(i));
      }
    }
    IIOMetadataNode node = new IIOMetadataNode(nodeName);
    rootNode.appendChild(node);
    return(node);
  }

  /**
   * Method to generate a GIF
   * @param images : LinkedList of BufferedImage, containing the images
   * @param outputPath : String specifying the path to the generated GIF file
   * @param frameDurationMs : int specifying the duration between frames in milliseconds
   * @param isCycling : boolean indicating if the animation should loop
   */
  public static void generateGif(LinkedList<BufferedImage> images, String outputPath, int frameDurationMs, boolean isCycling) throws Exception {
    if (images.isEmpty()) {
      throw new IllegalArgumentException("Image list is empty.");
    }
    
    // Create a new ImageOutputStream with the specified output path
    ImageOutputStream output = new FileImageOutputStream(new File(outputPath));
    
    // Create a GifSequenceWriter with the type of the first image, specified frame duration, and looping option
    GifSequenceWriter writer = new GifSequenceWriter(output, images.getFirst().getType(), frameDurationMs, isCycling);
    
    // Write each image in the sequence
    for (BufferedImage image : images) {
      writer.writeToSequence(image);
    }
    
    // Close the writer and output stream
    writer.close();
    output.close();
  }
}