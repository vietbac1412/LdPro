package tamhoang.ldpro4.data.local

import android.content.SharedPreferences
import android.content.res.Resources
import timber.log.Timber
import tamhoang.ldpro4.R
import tamhoang.ldpro4.data.constants.Constants
import tamhoang.ldpro4.data.model.*
import tamhoang.ldpro4.print.PrintType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 22/02/2018.
 */
@Singleton
class PreferencesHelper
@Inject
constructor(private val mPref: SharedPreferences) {

    companion object {
        const val PREF_FILE_NAME = "android_boilerplate_pref_file"
        // HOST
        const val PREF_HOST_NAME          = "android_boilerplate_host_name"
        const val VALUE_LAST_HOST_POS365  = ".pos365.vn"
        const val VALUE_HOST_NAME_DEFAULT = "default$VALUE_LAST_HOST_POS365"
        const val PREF_LAST_HOST_NAME   = "pref_last_host_name"
        const val VALUE_LAST_HOST_BANHANG365  = ".banhang365.vn"
        // USER
        const val PREF_APP_ID        = "android_app_id_multi_app"
        const val PREF_USER_NAME     = "android_boilerplate_pref_user_name"
        const val PREF_USER_PASSWORD = "android_boilerplate_pref_user_password"
        // SESSION
        const val PREF_SESSION_ID      = "android_boilerplate_session_id"
        const val PREF_SESSION_SETTING = "pref_session_setting"
        const val PREF_PRINTER_SETTING = "pref_printer_setting"
        // VN PAY
        const val PREF_LAYOUT_QR_CODE  = "pref_setting_pay_qr_code"
        const val PREF_ACTIVE_QR_CODE  = "pref_active_qr_code"
        const val PREF_CONNECT_POS = "pref_connect_pos"
        const val PREF_PRINT_BEFORE_SUCCESSFUL_PAYMENT = "pref_print_before_successful_payment"
        const val PREF_MERCHANT_CODE   = "pref_merchant_code"
        const val PREF_MERCHANT_NAME   = "pref_merchant_name"
        const val PREF_MERCHANT_CATEGORY_CODE = "pref_merchant_category_code"
        //VIETTEL PAY
        const val PREF_PAYMENT = "pref_payment"
        const val PREF_MERCHANT_CODE_VT   = "pref_merchant_code_vt"
        const val PREF_MERCHANT_NAME_VT   = "pref_merchant_name_vt"
        // NOTIFICATION
        const val PREF_LAYOUT_NOTIFICATIONS_RING = "pref_notifications"
        const val PREF_NOTIFICATIONS_PAY         = "pref_notifications_pay"
        // SYNC GATEWAY
        const val PREF_LAYOUT_SYNC_GATEWAY = "pref_sync_gateway"
        const val PREF_TYPE_SYNC_GATEWAY   = "pref_type_sync_gateway"
        // PRINT
        const val PREF_PRINT_TYPE_CASHIER    = "pref_print_type_cashier"
        const val PREF_PRINT_TYPE_KITCHEN_A  = "pref_print_type_kitchen_a"
        const val PREF_PRINT_TYPE_KITCHEN_B  = "pref_print_type_kitchen_b"
        const val PREF_PRINT_TYPE_KITCHEN_C  = "pref_print_type_kitchen_c"
        const val PREF_PRINT_TYPE_KITCHEN_D  = "pref_print_type_kitchen_d"
        const val PREF_PRINT_TYPE_BARTENDER_A = "pref_print_type_bartender_a"
        const val PREF_PRINT_TYPE_BARTENDER_B = "pref_print_type_bartender_b"
        const val PREF_PRINT_TYPE_BARTENDER_C = "pref_print_type_bartender_c"
        const val PREF_PRINT_TYPE_BARTENDER_D = "pref_print_type_bartender_d"
        const val PREF_PRINT_TYPE_TEMP       = "pref_print_type_temp"
        const val PREF_PRINT_TYPE_BLUETOOTH  = "pref_print_type_bluetooth"
        const val PREF_PRINT_TYPE_USB        = "pref_print_type_usb"
        // INFORMATION
        const val PREF_PRINT_INFORMATION         = "pref_print_information"
        const val PREF_PRINT_INFORMATION_NAME    = "pref_print_information_name"
        const val PREF_PRINT_INFORMATION_IMAGE   = "pref_print_information_image"
        const val PREF_PRINT_INFORMATION_FOOTER  = "pref_print_information_footer"
        const val PREF_PRINT_INFORMATION_ADDRESS = "pref_print_information_address"
        const val PREF_PRINT_INFORMATION_PHONE   = "pref_print_information_phone"
        const val PREF_PRINT_INFORMATION_BANNER = "pref_print_information_banner"
        const val PREF_PRINT_INFORMATION_SLIDE_SHOW = "pref_print_information_slide_show"
        //
        const val PREF_AUTO_PRINT_COOK         = "pref_auto_print_cook"
        const val PREF_AUTO_PRINT_TEMP_FROM_SERVED = "pref_auto_print_temp_from_served"
        const val PREF_PRINT_AFTER_PAY         = "pref_print_after_pay"
        const val PREF_PRINT_TEMP_AFTER_PAY         = "pref_print_temp_after_pay"
        const val PREF_OPEN_CASH_BOX_AFTER_PAY = "pref_open_cash_box_after_pay"
        const val PREF_ALLOW_PRINT_PREVIEW     = "pref_print_allow_print_preview"
        const val PREF_PRINT_HIDDEN_MODE     = "pref_print_hidden_mode"
        const val PREF_PRINT_KITCHEN_AFTER_SAVE = "pref_print_kitchen_after_save"
        const val PREF_STOCK_CONTROL_WHEN_SELLING = "pref_stock_control_when_selling"
        const val PREF_ALLOW_CHANGE_PRICE      = "pref_allow_change_price"
        const val PREF_VAT = "pref_vat"
        const val PREF_CATEGORIES_DIRECTION  = "pref_categories_direction"
        const val PREF_MENU_ITEM_SIZE          = "pref_menu_item_size"
        const val PREF_PRINT_COOK_SEPARATE_MENU = "pref_print_cook_separate_menu"
        //
        const val PREF_PRINT_TWO_FOR_INVOICE = "pref_print_two_for_invoice"
        const val PREF_PRINT_TWO_FOR_COOK    = "pref_print_two_for_cook"
        //
        const val PREF_PRINT_SIZE            = "pref_print_size"  // HIDE
        const val PREF_PRINT_IS_CUT          = "pref_print_is_cut"// HIDE
        //POINT
        const val PREF_POINT_CALCULATION = "pref_point_calculation"
        const val PREF_POINT_REDEEM = "pref_point_redeem"

        // VOUCHER
        const val PREF_VOUCHER = "pref_voucher"

        // SCREEN
        const val PREF_SCREEN_TYPE      = "pref_screen_type"
        const val VALUE_SCREEN_CASHIERS   = 0x0000de
        const val VALUE_SCREEN_ORDER      = 0x0000ef
        const val VALUE_SCREEN_SELF_ORDER = 0x0000fa
        const val PREF_FIELD_ID_BRANCH = "pref_field_id_branch"
        // BRANCH
        const val PREF_CURRENT_OFF_BRANCH_ID  = "Branch_%1\$s_%2\$s"
        const val PREF_CURRENT_BRANCH_NAME    = "pref_current_branch_name"
        const val PREF_BRANCH_OFF_KEY_DEFAULT       = "Branch_0_0"
        const val PREF_BRANCH_OFF_KEY_START_DEFAULT = "Branch_"

        const val VALUE_UNLIMITED = "unlimited"
        const val PREF_POINT_TO_VALUE = "pref_point_to_value"
        const val PREF_IP_DEFAULT = "pref_ip_default"

        const val PREF_CURRENCY_UNIT = "pref_currency_unit"
        const val PREF_PRINT_COOK_CACHE = "pref_print_cook_cache"

        const val PREF_SCREEN_SELF_ORDER_TYPE = "pref_screen_self_order_type"

        const val PREF_PRINT_TEMP_FIRST_PAY = "pref_print_temp_first_pay"

        const val PREF_PRINT_HTML = "pref_print_html"

        const val PREF_PRINT_HTML_COOK = "pref_print_html_cook"
        const val PREF_PRINT_HTML_PARTNER = "pref_print_html_partner"
        const val PREF_PRINT_HTML_VOUCHER = "pref_print_html_voucher"

        const val PREF_PRINT_PARTNER_HTML = "pref_print_partner_html"
        const val PREF_PRINT_VOUCHER_HTML = "pref_print_voucher_html"

        const val PREF_PRINT_TEMP = "pref_print_temp"

        const val PREF_USE_TWO_SCREEN = "pref_use_two_screen"

        const val PREF_EXTRA_EXIST = "pref_extra_exist"

        //-----------Ver 2---------------
        // INFORMATION TO PRINT
        const val PREF_SYNC_INFORMATION = "pref_sync_information"

        const val PREF_POINT_CONFIG = "pref_point_config"

        const val PREF_SHOW_NAVIGATION_BAR = "pref_show_navigation_bar"

        const val PREF_KEEP_SCREEN_ON ="pref_keep_screen_on"

        const val PREF_USING_DEFAULT_KEYBOARD = "pref_using_default_keyboard" //Only hw960 screen

        const val PREF_ALLOW_CHANGE_NAME_PRODUCT = "pref_allow_change_name_product"

        const val PREF_NOT_PRINT_ZERO_PRICE = "pref_not_print_zero_price"

        const val PREF_WIDTH_TO_PRINT = "pref_width_to_print"

        const val PREF_CARD_NUMBER_COUNT = "pref_card_number_count"

        const val PREF_QUICK_PAYMENT = "pref_quick_payment"

        const val PREF_RECEIVE_MESSAGE_FROM_SERVER = "pref_receive_message_from_server"
        const val PREF_RECEIVE_DATA_FROM_OTHER_CASHIERS ="pref_receive_data_from_other_cashiers"

        const val PREF_VIDEO_QUALITY = "pref_video_quality"

        const val PREF_BARCODE_SCAN_FULL_SCREEN = "pref_barcode_scan_full_screen"

        const val PREF_VERSION = "pref_version"
        const val VERSION = 3

        const val LATEST_SYNC = "latest_sync"
    }

    //---------VERSION: Thiet lap khi muon dat lai cac gia tri mac dinh--------

    private fun getVersion() = getInt(PREF_VERSION, 1)
    private fun putVersion(version: Int) = putInt(PREF_VERSION, version)

    fun onUpgrade(){
        if (VERSION > getVersion()) {
            //set default value auto print setting
            if (getVersion() < 3) {
                putBoolean(PREF_AUTO_PRINT_COOK, android.os.Build.MODEL in listOf(Constants.DEVICES_NAME_T2, Constants.DEVICES_NAME_T2_MINI, Constants.DEVICES_NAME_T2_PRO, Constants.DEVICES_NAME_T1))
            }
            //
            putVersion(VERSION)
        }
    }

    //=========================================================================

    private val mEdit = mPref.edit()

    private fun getString(key: String, default: String = ""): String = mPref.getString(key, default) ?: ""
    private fun putString(key: String, value: String) {
        mEdit.putString(key, value)
        mEdit.commit()
    }

    private fun getBoolean(key: String, default: Boolean = false) : Boolean = mPref.getBoolean(key, default)
    private fun putBoolean(key: String, value: Boolean) {
        mEdit.putBoolean(key, value)
        mEdit.commit()
    }

    private fun getInt(key: String, default: Int) = mPref.getInt(key, default)
    private fun putInt(key: String, value: Int) {
        mEdit.putInt(key, value)
        mEdit.commit()
    }

    private fun getFloat(key: String, default: Float) = mPref.getFloat(key, default)
    private fun putFloat(key: String, value: Float) {
        mEdit.putFloat(key, value)
        mEdit.commit()
    }

    fun putHostName(value: String) = putString(PREF_HOST_NAME, value.plus(getLastHostName()))
    fun getHostName(): String = getString(PREF_HOST_NAME, VALUE_HOST_NAME_DEFAULT)

    fun putSessionId(value: String) = putString(PREF_SESSION_ID, value)
    fun getSessionId(): String = getString(PREF_SESSION_ID, "")

    fun putAppId(value: String) = putString(PREF_APP_ID, value)
    fun getAppId(): String = getString(PREF_APP_ID, Constants.APP_CASHIER_ID)

    fun putUserName(value: String) = putString(PREF_USER_NAME, value)
    fun getUserName(): String = getString(PREF_USER_NAME, "")

    fun putUserPassword(value: String) = putString(PREF_USER_PASSWORD, value)
    fun getUserPassword(): String = getString(PREF_USER_PASSWORD, "")

    fun getNotificationsPay()   = getBoolean(PREF_NOTIFICATIONS_PAY, true)

    fun putIpDefault(value: String) = putString(PREF_IP_DEFAULT, value)
    fun getIpDefault(): String = getString(PREF_IP_DEFAULT, "192.168.0.")
    private fun setIpDefault() = "${getIpDefault()}1"

    fun putTypeSyncGateway(value: String) = putString(PREF_TYPE_SYNC_GATEWAY, value)
    fun getTypeSyncGateway() = getString(PREF_TYPE_SYNC_GATEWAY, setIpDefault())

    fun putPrintInformationName(value: String) = putString(PREF_PRINT_INFORMATION_NAME, value)
    fun getPrintInformationNameRetailer() = getString(PREF_PRINT_INFORMATION_NAME, getCurrentRetailer().name)
    fun getPrintInformationName() = getString(PREF_PRINT_INFORMATION_NAME, getCurrentBranchName())
    fun putPrintInformationImage(value: String) = putString(PREF_PRINT_INFORMATION_IMAGE, value)
    fun getPrintInformationImage() = getString(PREF_PRINT_INFORMATION_IMAGE, "")
    fun putPrintInformationAddress(value: String) = putString(PREF_PRINT_INFORMATION_ADDRESS, value)
    fun getPrintInformationAddress() = getString(PREF_PRINT_INFORMATION_ADDRESS, getCurrentRetailer().address)
    fun putPrintInformationPhone(value: String) = putString(PREF_PRINT_INFORMATION_PHONE, value)
    fun getPrintInformationPhone() = getString(PREF_PRINT_INFORMATION_PHONE, getCurrentRetailer().phone)
    fun putPrintInformationFooter(value: String) = putString(PREF_PRINT_INFORMATION_FOOTER, value)
    fun getPrintInformationFooter(resources: Resources) =
            getString(PREF_PRINT_INFORMATION_FOOTER, resources.getString(R.string.cam_on_hen_gap_lai))
    fun getPrintInformationFooter() =
            getString(PREF_PRINT_INFORMATION_FOOTER, "")

    fun getAutoPrintCook():Boolean = getBoolean(PREF_AUTO_PRINT_COOK, false)
    fun getAutoPrintTempFromServed() : Boolean = getBoolean(PREF_AUTO_PRINT_TEMP_FROM_SERVED, false)
    fun putPrintAfterPay(value: Boolean) = putBoolean(PREF_PRINT_AFTER_PAY, value)
    fun getPrintAfterPay()       = getBoolean(PREF_PRINT_AFTER_PAY, true)
    fun putPrintTempAfterPay(value: Boolean) = putBoolean(PREF_PRINT_TEMP_AFTER_PAY, value)
    fun getPrintTempAfterPay()   = getBoolean(PREF_PRINT_TEMP_AFTER_PAY, true)
    fun getOpenCashBoxAfterPay() = getBoolean(PREF_OPEN_CASH_BOX_AFTER_PAY, true)
    fun getPrintTempFirstPay()   = getBoolean(PREF_PRINT_TEMP_FIRST_PAY, false)
    fun getAllowPrintPreview()   = getBoolean(PREF_ALLOW_PRINT_PREVIEW, true)
    fun putAllowPrintPreview(value: Boolean) = putBoolean(PREF_ALLOW_PRINT_PREVIEW, value)
    fun getPrintHiddenMode()     = getBoolean(PREF_PRINT_HIDDEN_MODE, true)
    fun putPrintHiddenMode(value: Boolean) = putBoolean(PREF_PRINT_HIDDEN_MODE, value)
    fun getStockControlWhenSelling() = getBoolean(PREF_STOCK_CONTROL_WHEN_SELLING, false)
    fun putStockControlWhenSelling(value: Boolean) = putBoolean(PREF_STOCK_CONTROL_WHEN_SELLING, value)
    fun getPrintTwoForInvoice()  = getBoolean(PREF_PRINT_TWO_FOR_INVOICE)
    fun putPrintTwoForInvoice(value: Boolean) = putBoolean(PREF_PRINT_TWO_FOR_INVOICE, value)
    fun getPrintTwoForCook()     = getBoolean(PREF_PRINT_TWO_FOR_COOK)
    fun getPrintSize()           = getBoolean(PREF_PRINT_SIZE)
    fun getPrintIsCut()          = getBoolean(PREF_PRINT_IS_CUT, true)
    fun getAllowChangePrice() = getBoolean(PREF_ALLOW_CHANGE_PRICE, false)
    fun putAllowChangePrice(value: Boolean) = putBoolean(PREF_ALLOW_CHANGE_PRICE, value)
    fun getPrintKitchenAfterSave()   = getBoolean(PREF_PRINT_KITCHEN_AFTER_SAVE, false)
    fun putPrintKitchenAfterSave(value: Boolean) = putBoolean(PREF_PRINT_KITCHEN_AFTER_SAVE, value)
    fun getReceiveMessageFromServer() = getBoolean(PREF_RECEIVE_MESSAGE_FROM_SERVER, false)
    fun getReceiveDataFromOtherCashier() = getBoolean(PREF_RECEIVE_DATA_FROM_OTHER_CASHIERS, true)

    fun getAllowChangeNameProduct() = getBoolean(PREF_ALLOW_CHANGE_NAME_PRODUCT, false)
    fun putAllowChangeNameProduct(value: Boolean) = putBoolean(PREF_ALLOW_CHANGE_NAME_PRODUCT, value)

    fun getUseTwoScreen() = getBoolean(PREF_USE_TWO_SCREEN, false)

    fun getCurrentBranchId(): Int = getSessionSetting().currentBranchId

    fun getKeyOffBranch() = PREF_CURRENT_OFF_BRANCH_ID.format(getCurrentRetailer().id, getCurrentUser().id)
    fun putCurrentBranchOffId(value: Int) {
        val offKeyBranch = getKeyOffBranch()
        if (offKeyBranch != PREF_BRANCH_OFF_KEY_DEFAULT) { putInt(getKeyOffBranch(), value) }
    }
    fun getCurrentBranchOffId() = mPref.all
            .filter { it.key.startsWith(PREF_BRANCH_OFF_KEY_START_DEFAULT) }
            .toList()

    fun putCurrentBranchName(value: String) = putString(PREF_CURRENT_BRANCH_NAME, value)
    fun getCurrentBranchName(): String = getString(PREF_CURRENT_BRANCH_NAME, "")

    fun getScreenType(): Int = getInt(PREF_SCREEN_TYPE, VALUE_SCREEN_CASHIERS)
    fun putScreenType(value: Int) = putInt(PREF_SCREEN_TYPE, value)

    fun getFieldIdBranch(): Int = getInt(PREF_FIELD_ID_BRANCH, -1)
    fun putFieldIdBranch(value: Int) = putInt(PREF_FIELD_ID_BRANCH, value)

    fun putSessionSetting(value: Session) {
        putCurrentBranchOffId(value.currentBranchId)
        putString(PREF_SESSION_SETTING, value.toString())
    }
    fun getSessionSetting(): Session {
        val content = getString(PREF_SESSION_SETTING, Session().toString())
        return Session.convertStringToObject(content)
    }

    fun getChannels(): MutableList<Channel> {
        val channels = mutableListOf<Channel>()
        val channelsAddition = getSessionSetting().channels
        channels.add(Channel(id = 1, name = "Bán hàng trực tiếp"))
        channels.add(Channel(id = 2, name = "Bán qua điện thoại"))
        channels.add(Channel(id = 3, name = "GrabFood"))
        channels.add(Channel(id = 4, name = "Now"))
        channels.add(Channel(id = 5, name = "Pasgo"))
        channels.add(Channel(id = 6, name = "Go-Viet"))
        channels.add(Channel(id = 7, name = "Facebook"))
        channels.addAll(channelsAddition)
        return channels
    }

    fun putPrinterSetting(value: Printer365) {
        putString(PREF_PRINTER_SETTING, value.toString())
    }
    fun getPrinterSetting(): Printer365 {
        val content = getString(PREF_PRINTER_SETTING, Printer365().toString())
        return Printer365.convertStringToObject(content)
    }
    fun putPrintCookCache(value: PrintCookCache) {
        putString(PREF_PRINT_COOK_CACHE, value.toString())
    }
    fun getPrintCookCache(): PrintCookCache {
        val content = getString(PREF_PRINT_COOK_CACHE, PrintCookCache().toString())
        return PrintCookCache.convertStringToObject(content)
    }

    fun getCurrentUser() = getSessionSetting().currentUser
    fun getCurrentRetailer() = getSessionSetting().currentRetailer

    fun putCurrencyUnit(value: String) = putString(PREF_CURRENCY_UNIT, value)
    fun getCurrencyUnit(): String = getString(PREF_CURRENCY_UNIT, Constants.CURRENCY_UNIT_VIETNAME)
    fun checkCurrencyVnd() = getCurrencyUnit() == Constants.CURRENCY_UNIT_VIETNAME

    fun putSelfOrderType(value: Int) = putInt(PREF_SCREEN_SELF_ORDER_TYPE, value)
    fun getSelfOrderType(): Int = getInt(PREF_SCREEN_SELF_ORDER_TYPE, SelfOrderType.PAGE.value)

    fun putHtmlPrint(value: String) = putString(PREF_PRINT_HTML, value)
    fun getHtmlPrint(): String = getString(PREF_PRINT_HTML, Constants.HTML_PRINT_DEFAULT)

    fun putHtmlPrintCook(value: String) = putString(PREF_PRINT_HTML_COOK, value)
    fun putHtmlPrintPartner(value: String) = putString(PREF_PRINT_HTML_PARTNER, value)
    fun putHtmlPrintVoucher(value: String) = putString(PREF_PRINT_VOUCHER_HTML, value)
    fun getHtmlPrintCook(): String = getString(PREF_PRINT_HTML_COOK, Constants.HTML_PRINT_COOK_DEFAULT)
    fun getHtmlPrintPartner(): String = getString(PREF_PRINT_PARTNER_HTML,Constants.HTML_PRINT_CUSTOMER)
    fun getHtmlPrintVoucher(): String = getString(PREF_PRINT_VOUCHER_HTML,Constants.HTML_PRINT_VOUCHER)
    fun putTempPrint(value: String) = putString(PREF_PRINT_TEMP, value)
    fun getTempPrint(): String = getString(PREF_PRINT_TEMP, Constants.TEMP_PRINT_DEFAULT)

    fun clearAll() {
        mEdit.clear()
        mEdit.commit()
    }

    fun clear() {
        putSessionId("")
        putSessionSetting(Session())
        putUserPassword("")
        putLatestSync("")
    }

    fun getPrinterWaitForConfirmation(printerName: String = "") : Printer {
        val print365 = getPrinterSetting()
        return when(printerName) {
            Constants.PRINT_KITCHEN_A -> print365.kitchenA
            Constants.PRINT_KITCHEN_B -> print365.kitchenB
            Constants.PRINT_KITCHEN_C -> print365.kitchenC
            Constants.PRINT_KITCHEN_D -> print365.kitchenD
            Constants.PRINT_BARTENDER_A -> print365.bartenderA
            Constants.PRINT_BARTENDER_B -> print365.bartenderB
            Constants.PRINT_BARTENDER_C -> print365.bartenderC
            Constants.PRINT_BARTENDER_D -> print365.bartenderD
            Constants.PRINT_TEMP -> print365.temp
            else -> print365.cashier
        }
    }

    fun getMessagePrintFail(resources: Resources, printer: Printer = Printer()) : String {
        return when(printer.id) {
            PrintType.NO_PRINT.value -> {
                resources.getString(R.string.thiet_lap_khong_in)
            }
            PrintType.LOCAL_PRINT.value -> {
                resources.getString(R.string.chon_thiet_lap_tich_hop_may_in_local)
            }
            PrintType.LAN_PRINT.value -> {
                String.format(
                        resources.getString(R.string.kiem_tra_ket_noi_may_in),
                        printer.value
                )
            }
            PrintType.BLUETOOTH_PRINT.value -> {
                String.format(
                        resources.getString(R.string.kiem_tra_ket_noi_bluetooth),
                        printer.value
                )
            }
            PrintType.USB_PRINT.value -> {
                String.format(
                        resources.getString(R.string.kiem_tra_ket_noi_may_in_usb),
                        printer.value
                )
            }
            else -> {
                resources.getString(R.string.chon_thiet_lap_tich_hop_may_in_local)
            }
        }
    }

    fun isScreenRestaurant(): Boolean {

        if (getFieldIdBranch() != -1)
            return  getFieldIdBranch() == Constants.CURRENT_RETAILER_FIELD_FND_03 ||
                    getFieldIdBranch() == Constants.CURRENT_RETAILER_FIELD_FND_11
        else {
            val session = getSessionSetting()
            session.let {
                val currentBranchSession = it.branchs.firstOrNull{ branchSession ->
                    branchSession.id == it.currentBranchId
                } ?: BranchSession()

                val currentBranchFieldId = if(currentBranchSession.fieldId != -1) currentBranchSession.fieldId else it.currentRetailer.fieldId
                putFieldIdBranch(currentBranchFieldId)
                return currentBranchFieldId == Constants.CURRENT_RETAILER_FIELD_FND_03 ||
                        currentBranchFieldId == Constants.CURRENT_RETAILER_FIELD_FND_11
            }
        }
    }

    fun isScreenShop() = !isScreenRestaurant()

    fun formatNameDB(): String {
        val typeScreen = if (isScreenShop()) "SHOP" else "RESTAURANT"
        val output = "db$typeScreen${ getHostName() }(${ getCurrentBranchId() })"
        Timber.e("DB NAME ---> $output")
        return output
    }

    fun putSlideShow(value: SlideShow) {
        putString(PREF_PRINT_INFORMATION_SLIDE_SHOW, value.toString())
    }
    fun getSlideShow(): SlideShow {
        val content = getString(PREF_PRINT_INFORMATION_SLIDE_SHOW, SlideShow().toString())
        return SlideShow.convertStringToObject(content)
    }

    fun putBanner(value: Banner) {
        putString(PREF_PRINT_INFORMATION_BANNER, value.toString())
    }
    fun getBanner(): Banner {
        val content = getString(PREF_PRINT_INFORMATION_BANNER, Banner().toString())
        return Banner.convertStringToObject(content)
    }
    fun getBannerItem(): String {
        val banners = getBanner().getList()

        val number = (0..2).random()
        var output =  when (number) {
            0 -> banners[0]
            1 -> banners[1]
            else -> banners[0]
        }
        if (output.isNullOrEmpty()) output = Constants.BANNER_DEFAULT
        return output
    }

    //----------------Ver 2------------------
    fun putSyncInformation(value : Boolean)    = putBoolean(PREF_SYNC_INFORMATION, value)
    fun getSyncInformation()    = getBoolean(PREF_SYNC_INFORMATION, false)

    fun putUseTwoScreen(value : Boolean) = putBoolean(PREF_USE_TWO_SCREEN, value)

    fun getShowNavigationBar() = getBoolean(PREF_SHOW_NAVIGATION_BAR, false)

    fun getKeepScreenOn() = getBoolean(PREF_KEEP_SCREEN_ON, true)

    fun getUsingDefaultKeyboard() = getBoolean(PREF_USING_DEFAULT_KEYBOARD, true)

    fun getLastHostName() = getString(PREF_LAST_HOST_NAME, VALUE_LAST_HOST_POS365)
    fun putLastHostName(value: String = VALUE_LAST_HOST_POS365) = putString(PREF_LAST_HOST_NAME, value)

    fun getConnectPOS() = getBoolean(PREF_CONNECT_POS, false)
    fun putConnectPOS(value: Boolean) = putBoolean(PREF_CONNECT_POS, value)

    fun getPrintBeforeSuccessfulPayment() = getBoolean(PREF_PRINT_BEFORE_SUCCESSFUL_PAYMENT, false)
    fun putPrintBeforeSuccessfulPayment(value: Boolean) = putBoolean(PREF_PRINT_BEFORE_SUCCESSFUL_PAYMENT, value)

    fun getNotPrintZeroPrice() = getBoolean(PREF_NOT_PRINT_ZERO_PRICE, false)
    fun putNotPrintZeroPrice(value: Boolean) = putBoolean(PREF_NOT_PRINT_ZERO_PRICE, value)

    fun getWithToPrint(): String {
        val widthPrintDefault = when (android.os.Build.MODEL) {
            Constants.DEVICES_NAME_V1S -> "58mm"
            Constants.DEVICES_NAME_V2_PRO -> "58mm"
            Constants.DEVICES_NAME_P1_4G_EU -> "58mm"
            Constants.DEVICES_NAME_T1 -> "76mm"
            Constants.DEVICES_NAME_T2 -> "76mm"
            Constants.DEVICES_NAME_K1-> "76mm"
            else -> "58mm"
        }
        return getString(PREF_WIDTH_TO_PRINT, widthPrintDefault)
    }
    fun putWithToPrint(value: String) = putString(PREF_WIDTH_TO_PRINT, value)

    fun putCardNumberCount(value : Int)    = putInt(PREF_CARD_NUMBER_COUNT, value)
    fun getCardNumberCount()    = getInt(PREF_CARD_NUMBER_COUNT, 0)

    fun isSunmiDevices() : Boolean {
        val listSunmiDevices = listOf<String>(
                Constants.DEVICES_NAME_V1S,
                Constants.DEVICES_NAME_V2_PRO,
                Constants.DEVICES_NAME_P1_4G_EU,
                Constants.DEVICES_NAME_T1,
                Constants.DEVICES_NAME_T2,
                Constants.DEVICES_NAME_K1)
        return  android.os.Build.MODEL in listSunmiDevices
    }

    fun canPayByCard(settingScreen: Boolean = false): Boolean {
        val listSunmiDevices = listOf<String>(
                Constants.DEVICES_NAME_P1_4G_EU,
                Constants.DEVICES_NAME_P1_4G,
                Constants.DEVICES_NAME_P2,
                Constants.DEVICES_NAME_P2_PRO,
                Constants.DEVICES_NAME_P2_LITE,
                Constants.DEVICES_NAME_P2_MINI )
        val canBySetting = settingScreen || getConnectPOS()
        return  android.os.Build.MODEL in listSunmiDevices && canBySetting
    }

    //----------------NOT SETTING-----------------
    fun putExtraExist(value : Boolean)    = putBoolean(PREF_EXTRA_EXIST, value)
    fun getExtraExist()    = getBoolean(PREF_EXTRA_EXIST, false)

    fun putQuickPayment(value : Boolean)    = putBoolean(PREF_QUICK_PAYMENT, value)
    fun getQuickPayment()    = getBoolean(PREF_QUICK_PAYMENT, false)

    fun putCategoriesVertical(value : Boolean) = putBoolean(PREF_CATEGORIES_DIRECTION, value)
    fun getCategoriesVertical() = getBoolean(PREF_CATEGORIES_DIRECTION, true)

    fun putMenuItemSize(value : Int) = putInt(PREF_MENU_ITEM_SIZE, value)
    fun getMenuItemSize() = getInt(PREF_MENU_ITEM_SIZE, 2)

    fun putBarcodeScanFullScreen(value : Boolean) = putBoolean(PREF_BARCODE_SCAN_FULL_SCREEN, value)
    fun getBarcodeScanFullScreen() = getBoolean(PREF_BARCODE_SCAN_FULL_SCREEN, true)

    //----------------------------------------------

    fun getPrintCookSeparateMenu() = getBoolean(PREF_PRINT_COOK_SEPARATE_MENU, false)

    //---------------LATEST SYNC--------------------------
    fun putLatestSync(value : String) = putString(LATEST_SYNC, value)
    fun getLatestSync() = getString(LATEST_SYNC, "")
}