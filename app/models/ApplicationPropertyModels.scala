package models

/**
 * Represents groups of values of application properties.
 * Helps to pass only values to the view.
 */
object ApplicationPropertyModels {

  /**
   * Values of application properties which are interesting only for main page template.
   */
  case class MainTemplateProperties(instanceName: String,
                                    topBannerCodePenId: Option[String] = None,
                                    bottomBannerCodePenId: Option[String] = None,
                                    signUpAvailable: Boolean = false ) {

    def hasTopPageBanner = topBannerCodePenId.isDefined && topBannerCodePenId.get.startsWith("http://codepen.io/")
    def hasBottomPageBanner = bottomBannerCodePenId.isDefined && bottomBannerCodePenId.get.startsWith("http://codepen.io/")
  }

}
