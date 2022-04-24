package tamhoang.ldpro4.data

import tamhoang.ldpro4.data.local.PreferencesHelper
import tamhoang.ldpro4.data.model.*

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 27/03/2018.
 */
class BusEvent {
    class AuthenticationError
    class PartnerChooseCompleted(val partner: Partner)
    class PriceBookChooseCompleted(val treeView: TreeView, val pos: String)
    class TransformTable(val tableId: Int, val tableType: String)
    class ExtraToppingUpdate(val extra: Extra)
    class TableProductUpdateExtraTopping(val product: Product, val jsonContent: JsonContent)
    class UpdateStateRoom
    class TableProductUpdateNoteBook(val pos: String, val products: List<Product>)
    class PayFinish(val value: ServerEvents)
    class ChangeDiscount(val jsonContent: JsonContent)
    class ChangeVoucher(val jsonContent: JsonContent)
    class SyncGatewayUpdate(val realTime: RealTime)
    class WaitForConfirmOffline(val sendServeEntities: SendServeEntities)
    class ChangeTableOffline(val serveChangeTableEntities: ServeChangeTableEntities)

    class SendDataSignIn(val name: String, val account: String, val password: String)
    class SendDataRetail(val content: String)

    class EditTableAction
    class EditMenuAction
    class EditPartnerAction

    class ListenerEditTable(val type: Int, val room: Room? = null)
    class ListenerEditMenu(val type: Int, val product: Product? = null)
    class ListenerEditPartner(val type: Int, val partner: Partner? = null)

    class UpdateNavigationMenuCount(val count: Int)

    class SendDataAfterSearchComplete(val roomId: Int?, val pos: String, val products: List<Product>)

    class DeleteProduct(val pos: Int, val productId: Int)
    class InsertProduct(val pos: Int, val product: Product)
    class UpdateProduct(val pos: Int, val product: Product)
    class SendComponentProduct(val components: Components)

    class DeletePartner(val pos: Int, val partnerId: Int)
    class InsertPartner(val pos: Int, val partner: Partner)
    class UpdatePartner(val pos: Int, val partner: Partner)

    class ChooseShopAction(val room: Room)

    class CalculatorInputType(val type: Int, val result: String)

    class EditHtmlPrint(val html: String)

    class SelectOrUnselectInventoryProduct(val isSelect: Boolean,val productId: Int)
    class ChooseInventoryProduct(val product: Product)
    class LoadRepeatInventoryDetail()
    class ChooseInventoryCategory(val category: Categorie)
    class ActionReloadInventory(val inventoryResult : InventoryResult)

    class EditTempPrint(val content: String)

    class BackFromPayment(val serverEvents: ServerEvents)

    class SplitTable (val tableId: Int, val tableType: String)

    // OrderStock
    class SelectOrUnselectOrderStockProduct(val isSelect: Boolean,val productId: Int)
    class ChooseOrderStockProduct( val product: Product)
    class LoadRepeatOrderStockDetail()
    class ChooseOrderStockCategory(val category: Categorie)
    class ActionOrderStock(val orderStockResult : OrderStockResult)

    //Category
    class EditCategory(val category: Categorie, val pos: Int)
    class DeleteCategory(val category: Categorie, val pos: Int)

    //Group partner
    class AddGroupPartner(val group : PartnerGroup)
    class UpdateGroupPartner(val group: PartnerGroup, val pos: Int)
    class DeleteGroupPartner(val group: PartnerGroup, val pos: Int)

    //Group
    class AddSupplierGroup(val group : GroupMember)
    class UpdateSupplierGroup(val group: GroupMember, val pos: Int)
    class DeleteSupplierGroup(val group: GroupMember, val pos: Int)

    //Supplier
    class ReloadSupplier

    class ActionReload

    class DeleteVoucher()

    class NoteBookDetailChoose(val noteBookId: Int)

    class ChooseOneProduct(val product: Product)

    class ActionReturnProduct(val returnProductResult: ResultReturnProduct)

    //Count pending order
    class RecountOrder()
}