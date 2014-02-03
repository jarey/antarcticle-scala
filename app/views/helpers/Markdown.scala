package views.helpers

import java.io.{StringReader, StringWriter}
import org.pegdown.PegDownProcessor
import org.pegdown.Extensions._
import org.pegdown.VerbatimSerializer
import org.pegdown.LinkRenderer
import org.pegdown.Printer
import org.parboiled.common.StringUtils
import org.pegdown.ast.VerbatimNode
import org.pegdown.ToHtmlSerializer

/**
 * Adds class with specified in fenced code block language for
 * extended syntax highlighting using google prettify.
 *
 * Example:
 * Should produce: <pre><code class="lang-ruby">..</code></pre>
 * for markdown like:
 * ```ruby
 * ruby code here
 * ```
 */
object GooglePrettifyVerbatimSerializer extends VerbatimSerializer {
  override def serialize(node: VerbatimNode, printer: Printer): Unit = {
    println("SERIALIZE:" + node)
    printer.println().print("<pre><code")
    if (!StringUtils.isEmpty(node.getType)) {
      printAttribute(printer, "class", s"lang-${node.getType}")
    }
    printer.print(">")
    var text = node.getText
    // print HTML breaks for all initial newlines
    while (text.charAt(0) == '\n') {
      printer.print("<br/>")
      text = text.substring(1)
    }
    printer.printEncoded(text)
    printer.print("</code></pre>")

  }

  private def printAttribute(printer: Printer, name: String, value: String): Unit = {
    printer.print(' ').print(name).print('=').print('"').print(value).print('"')
  }
}

object Markdown {
  def toHtml(markdown: String): String = {
    // use all extensions
    val processor = new PegDownProcessor(ALL)
    // custom fenced code blocks serializer
    val serializerMap = new java.util.HashMap[String, VerbatimSerializer]
    serializerMap.put(VerbatimSerializer.DEFAULT, GooglePrettifyVerbatimSerializer)
    processor.markdownToHtml(markdown, new LinkRenderer(), serializerMap)
  }
}
