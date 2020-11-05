package codegen

import com.github.mustachejava.DefaultMustacheFactory

/**
 * @author afezeria
 */
abstract class Mustache {
  static def _mf = new DefaultMustacheFactory()

  def render() {
    def stream = this.class.classLoader.getResourceAsStream("${this.class.name.replace('.', '/')}.hbs")
    def template = _mf.compile(new InputStreamReader(stream), this.class.name)
    template.execute(
        outWriter(),
        this
    ).flush()
  }

  Writer outWriter() {
    new PrintWriter(System.out)
  }
}
