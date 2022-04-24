package tamhoang.ldpro4.util.extension

import java.util.regex.Pattern

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 2019-07-10.
 */
fun String.isYoutubeUrl(): Boolean {
    val pattern = Regex("^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+")
    return isNotEmpty() && matches(pattern)
}

fun String.getVideoIdFromYoutubeUrl(): String {
    /**
    Possibile Youtube urls.
    http://www.youtube.com/watch?v=WK0YhfKqdaI
    http://www.youtube.com/embed/WK0YhfKqdaI
    http://www.youtube.com/v/WK0YhfKqdaI
    http://www.youtube-nocookie.com/v/WK0YhfKqdaI?version=3&hl=en_US&rel=0
    http://www.youtube.com/watch?v=WK0YhfKqdaI
    http://www.youtube.com/watch?feature=player_embedded&v=WK0YhfKqdaI
    http://www.youtube.com/e/WK0YhfKqdaI
    http://youtu.be/WK0YhfKqdaI
     */

    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
    val compiledPattern = Pattern.compile(pattern)
    val matcher = compiledPattern.matcher(this)
    if (matcher.find()) return matcher.group()
    return ""
}