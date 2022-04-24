package tamhoang.ldpro4.data.remote

import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import tamhoang.ldpro4.data.constants.Constants
import tamhoang.ldpro4.data.model.*

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 22/02/2018.
 */
interface Pos365Service {
    companion object {
        const val CREDENTIAL = "api/auth/credentials?format=json"
        const val LOGOUT     = "api/auth/logout?format=json"
        const val ORDERS     = "api/orders?format=json"
    }

    @GET(CREDENTIAL)
    fun credentials(@Query("Username") account: String,
                    @Query("Password") password: String) : Observable<Any>

    @GET(LOGOUT)
    fun logout() : Completable

//    @GET("api/roomgroups/sync?format=json")
//    fun syncRoomGroups(@Query("LatestSync") latestSync : String) : Observable<RoomGroups>
//
//    @POST("api/roomgroups?format=json")
//    fun addNewRoomGroup(@Body sendRoomGroup: SendRoomGroup) : Observable<RoomGroup>
//
//    @GET("api/rooms/sync?format=json")
//    fun syncRooms(@Query("LatestSync") latestSync : String) : Observable<Rooms>
//
//    @POST("api/rooms?ExcludeStatus=true&format=json")
//    fun addNewRoom(@Body sendRoom: SendNewRoom) : Observable<Room>
//
//    @DELETE("api/rooms/{roomId}?format=json")
//    fun deleteRoom(@Path("roomId") roomId: Int) : Completable
//
//    @GET("api/products/sync?format=json")
//    fun syncProducts(@Query("LatestSync") latestSync : String) : Observable<Products>
//
//    @DELETE("api/products/{productId}?format=json")
//    fun deleteProduct(@Path("productId") productId: Int) : Completable
//
//    @POST("api/products?format=json")
//    fun insertOrUpdateProduct(@Body insertOrUpdateProduct: InsertOrUpdateProduct) : Observable<Product>
//
//    @GET("api/categories/sync?format=json")
//    fun syncCategories(@Query("LatestSync") latestSync : String) : Observable<Categories>
//
//    @POST("api/categories?format=json")
//    fun addNewCategory(@Body sendCategory: SendCategory) : Observable<Categorie>
//
//    @GET("api/serverevents?format=json")
//    fun syncServerEvents() : Observable<MutableList<ServerEvents>>
//
//    @FormUrlEncoded
//    @POST("Signin/RequestPassword")
//    fun forgotPassword(@Field ("Email") email : String): Completable
//
//    @GET("api/pricebooks/treeview?format=json")
//    fun syncPriceBookTreeView(): Observable<MutableList<TreeView>>
//
//    @GET("api/pricebooks?format=json")
//    fun syncPriceBookFullData(): Observable<ResultFullDataObject>
//
//    @GET("api/partners/sync?format=json")
//    fun syncPartner(@Query("LatestSync") latestSync : String): Observable<Partners>
//
//    @GET("api/partners/sync/{partnerId}?format=json")
//    fun getPartner(@Path("partnerId") partnerId: Int) : Observable<Partner>
//
//    @POST("api/partners?format=json")
//    fun insertOrUpdatePartner(@Body insertOrUpdatePartner: InsertOrUpdatePartner) : Observable<Partner>
//
//    @GET("api/notebooks?format=json")
//    fun getNoteBooks(): Observable<NoteBook>
//
//    @GET("api/notebooks/detail?format=json&Includes=Product")
//    fun getNoteBooksFollowId(@Query("NotebookId") id: Int): Observable<NoteBookFollowId>
//
//    @GET("api/products/sync/extraext?format=json")
//    fun getExtraFollowId(@Query("LatestSync") latestSync : String) : Observable<List<Extra>>
//
//    @GET("api/promotion?Includes=Product&?Includes=Product&Includes=Promotion&format=json")
//    fun getPromotion(@Query("inlinecount") inlineCount: String = "allpages",
//                     @Query("top") top: Int = Constants.VALUE_PAGE_QUERY_COUNT,
//                     @Query("skip") offset: Int = 0): Observable<Promotion>
//
//    @POST("api/promotion/add?format=json")
//    fun insertPromotion(@Body promotion: PromotionInsert): Completable
//
//    @POST("api/promotion/update?format=json")
//    fun updatePromotion(@Body promotion: PromotionUpdate): Completable
//
//    @DELETE("api/promotion/{Id}/delete?format=json")
//    fun deletePromotion(@Path("Id") id: Int): Completable
//
//    @GET("api/returns?format=json")
//    fun getReturnProduct(@Query("Includes") partner: String = "Partner",
//                         @Query("IncludeSummary") includeSummary: Boolean = false,
//                         @Query("inlinecount") inlineCount: String = "allpages",
//                         @Query("ProductCode") productCode: String? = null,
//                         @Query("top") top: Int = Constants.VALUE_PAGE_QUERY_COUNT,
//                         @Query("skip") offset: Int = 0,
//                         @Query(encoded = true, value = "filter") filter: String = ""): Observable<ReturnProduct>
//
//    @GET("api/returns/detail?format=json")
//    fun getReturnProductDetail(@Query("Includes") product : String = "Product",
//                               @Query("IncludeSummary") includeSummary: Boolean = true,
//                               @Query("ReturnId") returnId: Int,
//                               @Query("inlinecount") inlineCount: String = "allpages",
//                               @Query("top") top: Int = 10,
//                               @Query("skip") offset: Int = 0): Observable<ReturnProductDetail>
//
//    @GET("api/orders?format=json")
//    fun getReturnInvoiceList(@Query("Includes") partner: String = "Partner",
//                             @Query("Includes") room: String = "Room",
//                             @Query("ExcludeVoid") excludeVoid: Boolean = true,
//                             @Query("inlinecount") inlineCount: String = "allpages",
//                             @Query("ProductCode") productCode : String? = null,
//                             @Query("top") top: Int = 8,
//                             @Query("skip") offset: Int = 0,
//                             @Query(encoded = true, value = "filter") filter: String = ""): Observable<ReturnInvoiceList>
//
//    @GET("api/orders/detailforedit?format=json")
//    fun getProductOrder(@Query("OrderId") id: Int): Observable<List<Product>>
//
//    @DELETE("api/returns/{Id}/void?format=json")
//    fun cancelReturnProductDetail(@Path("Id") returnId: Int): Completable
//
//    @DELETE("api/returns/{Id}?format=json")
//    fun deleteReturnProductDetail(@Path("Id") returnId: Int): Completable
//
//    @POST("api/returns?format=json")
//    fun updateReturnProduct(@Body returnProduct: ReturnProductUpdate): Completable
//
//    @GET(ORDERS)
//    fun getListOrder(
//        @Query("Includes") partner: String = "Partner",
//        @Query("Includes") room: String = "Room",
//        @Query("IncludeSummary") includeSummary: Boolean = false,
//        @Query("inlinecount") inlineCount: String = "allpages",
//        @Query("top") limit: Int = Constants.VALUE_PAGE_QUERY_COUNT,
//        @Query("skip") offset: Int = 0,
//        @Query(encoded = true, value = "filter") filter: String = ""
//    ) : Observable<Order>
//
//    @DELETE("api/orders/{orderId}/void?format=json")
//    fun deleteOrder( @Path("orderId") orderId : Long ) : Completable
//
//    @GET("api/orders/detail?format=json")
//    fun getOrderDetail(
//        @Query("Includes") product: String = "Product",
//        @Query("IncludeSummary") includeSummary: Boolean = true,
//        @Query("OrderId") orderId: Int = 0,
//        @Query("inlinecount") inlineCount: String = "allpages",
//        @Query("top") limit: Int = Constants.VALUE_PAGE_QUERY_COUNT,
//        @Query("skip") offset: Int = 0
//    ) : Observable<OrderItemDetail>
//
//    @GET("api/orders/detail?format=json")
//    fun getOrderDetailAll(
//            @Query("Includes") product: String = "Product",
//            @Query("IncludeSummary") includeSummary: Boolean = true,
//            @Query("OrderId") orderId: Int = 0,
//            @Query("inlinecount") inlineCount: String = "allpages"
//    ) : Observable<OrderItemDetail>
//
//    @GET("api/products?format=json")
//    fun getOnlinePageProduct(
//        @Query("IncludeSummary") includeSummary: Boolean = true,
//        @Query("inlinecount") inlineCount: String = "allpages",
//        @Query("CategoryId") categoryId: Int = -1,
//        @Query("PartnerId") partnerId: Int = 0,
//        @Query("top") limit: Int = Constants.VALUE_PAGE_QUERY_COUNT,
//        @Query("skip") offset: Int = 0
//    ) : Observable<OnlinePageProduct>
//
//    @GET("api/rooms?format=json")
//    fun getOnlineRooms(
//            @Query("inlinecount") inlineCount: String = "allpages"
//    ) : Observable<OnlinePageRooms>
//
//    @GET("api/mobile/vendorsession?format=json")
//    fun getSession() : Observable<Session>
//
//    @GET("https://reporting.pos365.vn/api/reports/clients/172117-a715/instances/172117-de49/documents/172117-dc43172117-b428/pages/1")
//    fun getChart() : Observable<Chart>
//
//    @POST("api/serve?format=json")
//    fun sendServe(@Body serveEntities: SendServeEntities) : Completable
//
//    @POST("api/serve/changetable?format=json")
//    fun changeTable(@Body serveChangeTableEntities: ServeChangeTableEntities) : Completable
//
//    @GET("api/serve/changetableconfirmationall?format=json")
//    fun changeTableConfirmationAll() : Observable<List<ChangeTable>>
//
//    @POST("api/pricebooks/{priceBooksId}/manyproductprice?format=json")
//    fun manyProductPrice(@Path("priceBooksId") priceBookId: Int,
//                         @Body sendDataChangePartner: SendDataChangePartner) : Observable<ResponseManyProductPrice>
//
//    @GET("api/pricebooks/{priceBooksId}/productprice?format=json")
//    fun productPrice(@Path("priceBooksId") priceBookId: String,
//                     @Query("ProductId") orderId: Int = 0) : Observable<PriceList>
//
//    @POST("api/orders?format=json")
//    fun paymentAction(@Body paymentData: PaymentData) : Observable<PayResult>
//
//    @GET("api/orders/detailforedit?format=json")
//    fun getOrderDetailForEdit( @Query("OrderId") orderId: Int) : Observable<List<Product>>
//
//    @POST("api/notificationhub/sent?format=json")
//    fun paymentSend(@Body paymentSendMessage: PaymentSendMessage) : Completable
//
//    @GET("api/pingwithoutauth?format=json")
//    fun ping() : Observable<String>
//
//    @GET("api/serve/waitforconfirmation?format=json")
//    fun waitForConfirmation() : Observable<Int>
//
//    @GET("api/serve/waitforconfirmationall?format=json")
//    fun waitForConfirmationAll() : Observable<List<WaitForConfirmation>>
//
//    @GET("api/users/{userId}/privileges?format=json")
//    fun getPrivileges(@Path("userId") userId: Int,
//                      @Query("BranchId") branchId: Int) : Observable<Privileges>
//
//    @GET("api/mobile/branchs?format=json")
//    fun getBranchs() : Observable<List<Branch>>
//
//    @GET("api/branchs/{branchId}?format=json")
//    fun getBranch(@Path("branchId") branchId: Int) : Observable<Branch>
//
//    @POST("Home/ChangeBranch")
//    fun changeBranch(@Query("branchId") branchId: Int = 0) : Observable<Branch>
//
//    @POST("api/setting/updatesinglefeatureconfiguration?format=json")
//    fun updateSingleFeatureConfiguration(@Body sendInformationQRCode: SendInformationQRCode) : Completable
//
//    @POST("api/orders/{orderId}/changepayment?format=json")
//    fun changePayment(@Path("orderId") priceBookId: Long,
//                      @Body sendChangePayment: SendChangePayment) : Completable
//
//    @GET("api/orders/{orderId}/paymentstatus?format=json")
//    fun getStatusPayment(@Path("orderId") priceBookId: Long) : Observable<Boolean>
//
//    @POST("api/orders/multipaymentstatus?format=json")
//    fun getStatusMultiPayment(@Body multiPaymentPush: MultiPaymentPush) : Observable<List<ResultMultiPayment>>
//
//    @POST("api/rooms/history?format=json")
//    fun commitRoomHistory(@Body roomHistoryPush: RoomHistoryPush) : Completable
//
//    @GET("api/products/{productId}/currentonhand?format=json")
//    fun getCurrentOnHand(@Path("productId") productId: Int) : Observable<CurrentOnHand>
//
//    @Multipart
//    @POST("api/file/uploadimage?format=json")
//    fun uploadImage(@Part file: MultipartBody.Part) : Observable<List<UploadImageResult>>
//
//    @GET("api/partners/{partnerId}?Includes=PartnerGroupMembers&format=json")
//    fun getPartnerOnline(@Path("partnerId") partnerId: Int): Observable<PartnerOnline>
//
//    @GET("api/groups?format=json")
//    fun getGroupForPartner(@Query("Type") type: Int): Observable<List<PartnerGroup>>
//
//    @DELETE("api/partners/{partnerId}?format=json")
//    fun deletePartner(@Path("partnerId") partnerId: Int) : Completable
//
//    @GET("api/autocomplete/voucher?format=json")
//    fun searchVoucher(@Query("Keyword") keyword: String): Observable<List<Voucher>>
//
//    @GET("api/printtemplates/{number}?format=json")
//    fun getPrintFormatHtml(@Path("number") number: Int): Observable<HtmlPrint>
//
//    @POST("api/printtemplates?format=json")
//    fun pushPrintFormatHtml(@Body printTemplate: PrintTemplate): Completable
//
//    @GET("api/google/tocken?format=json")
//    fun getGoogleDriverToken() : Observable<Token>
//
//    @GET("api/voucher/getbycode?format=json")
//    fun confirmVoucherCode(@Query("Code") code: String) : Observable<Voucher?>
//
//    @GET("/mobile/endOfDay")
//    fun getReportLastDay(@Query("timeRange") time: String,
//                         @Query("allProduct") allProduct: Boolean = true,
//                         @Query("width") width: String,
//                         @Query("startDate") startDate: String? = null,
//                         @Query("endDate") endDate: String? = null) : Observable<Response<ResponseBody>>
}