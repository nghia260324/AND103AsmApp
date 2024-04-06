package com.ph41626.and103_assignment.Model;

import java.util.ArrayList;

public class GHNOrderDetail {
    private int shop_id;
    private int client_id;
    private String return_name;
    private long return_phone;
    private String return_address;
    private String return_ward_code;
    private int return_district_id;
    private GHNLocation return_location;
    private String from_name;
    private long from_phone;
    private String from_address;
    private String from_ward_code;
    private int from_district_id;
    private GHNLocation from_location;
    private int deliver_station_id;
    private String to_name;
    private long to_phone;
    private String to_address;
    private String to_ward_code;
    private int to_district_id;
    private GHNLocation to_location;
    private int weight;
    private int length;
    private int width;
    private int height;
    private int converted_weight;
    private String image_ids;
    private int service_type_id;
    private int service_id;
    private int payment_type_id;
    private ArrayList<Integer> payment_type_ids;
    private int custom_service_fee;
    private String sort_code;
    private int cod_amount;
    private String cod_collect_date;
    private String cod_transfer_date;
    private boolean is_cod_transferred;
    private boolean is_cod_collected;
    private int insurance_value;
    private int order_value;
    private int pick_station_id;
    private String client_order_code;
    private int cod_failed_amount;
    private String cod_failed_collect_date;
    private String required_note;
    private String content;
    private String note;
    private String employee_note;
    private String seal_code;
    private String pickup_time;
    private ArrayList<GHNItem> items;
    private String coupon;
    private String _id;
    private String order_code;
    private String version_no;
    private String updated_ip;
    private int updated_employee;
    private int updated_client;
    private String updated_source;
    private String updated_date;
    private int updated_warehouse;
    private String created_ip;
    private int created_employee;
    private int created_client;
    private String created_source;
    private String created_date;
    private String status;
    private int pick_warehouse_id;
    private int deliver_warehouse_id;
    private int current_warehouse_id;
    private int return_warehouse_id;
    private int next_warehouse_id;
    private String leadtime;
    private String order_date;
//    private String data;
    private String soc_id;
    private String s2r_time;
    private String return_time;
    private String finish_date;
//    private ArrayList<String> tag;
//    private boolean is_partial_return;
//    private ArrayList<String> partial_return;

    public GHNOrderDetail() {
    }

    public GHNOrderDetail(int shop_id, int client_id, String return_name, long return_phone, String return_address, String return_ward_code, int return_district_id, GHNLocation return_location, String from_name, long from_phone, String from_address, String from_ward_code, int from_district_id, GHNLocation from_location, int deliver_station_id, String to_name, long to_phone, String to_address, String to_ward_code, int to_district_id, GHNLocation to_location, int weight, int length, int width, int height, int converted_weight, String image_ids, int service_type_id, int service_id, int payment_type_id, ArrayList<Integer> payment_type_ids, int custom_service_fee, String sort_code, int cod_amount, String cod_collect_date, String cod_transfer_date, boolean is_cod_transferred, boolean is_cod_collected, int insurance_value, int order_value, int pick_station_id, String client_order_code, int cod_failed_amount, String cod_failed_collect_date, String required_note, String content, String note, String employee_note, String seal_code, String pickup_time, ArrayList<GHNItem> items, String coupon, String _id, String order_code, String version_no, String updated_ip, int updated_employee, int updated_client, String updated_source, String updated_date, int updated_warehouse, String created_ip, int created_employee, int created_client, String created_source, String created_date, String status, int pick_warehouse_id, int deliver_warehouse_id, int current_warehouse_id, int return_warehouse_id, int next_warehouse_id, String leadtime, String order_date, String data, String soc_id, String s2r_time, String return_time, String finish_date, ArrayList<String> tag, boolean is_partial_return, ArrayList<String> partial_return) {
        this.shop_id = shop_id;
        this.client_id = client_id;
        this.return_name = return_name;
        this.return_phone = return_phone;
        this.return_address = return_address;
        this.return_ward_code = return_ward_code;
        this.return_district_id = return_district_id;
        this.return_location = return_location;
        this.from_name = from_name;
        this.from_phone = from_phone;
        this.from_address = from_address;
        this.from_ward_code = from_ward_code;
        this.from_district_id = from_district_id;
        this.from_location = from_location;
        this.deliver_station_id = deliver_station_id;
        this.to_name = to_name;
        this.to_phone = to_phone;
        this.to_address = to_address;
        this.to_ward_code = to_ward_code;
        this.to_district_id = to_district_id;
        this.to_location = to_location;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.converted_weight = converted_weight;
        this.image_ids = image_ids;
        this.service_type_id = service_type_id;
        this.service_id = service_id;
        this.payment_type_id = payment_type_id;
        this.payment_type_ids = payment_type_ids;
        this.custom_service_fee = custom_service_fee;
        this.sort_code = sort_code;
        this.cod_amount = cod_amount;
        this.cod_collect_date = cod_collect_date;
        this.cod_transfer_date = cod_transfer_date;
        this.is_cod_transferred = is_cod_transferred;
        this.is_cod_collected = is_cod_collected;
        this.insurance_value = insurance_value;
        this.order_value = order_value;
        this.pick_station_id = pick_station_id;
        this.client_order_code = client_order_code;
        this.cod_failed_amount = cod_failed_amount;
        this.cod_failed_collect_date = cod_failed_collect_date;
        this.required_note = required_note;
        this.content = content;
        this.note = note;
        this.employee_note = employee_note;
        this.seal_code = seal_code;
        this.pickup_time = pickup_time;
        this.items = items;
        this.coupon = coupon;
        this._id = _id;
        this.order_code = order_code;
        this.version_no = version_no;
        this.updated_ip = updated_ip;
        this.updated_employee = updated_employee;
        this.updated_client = updated_client;
        this.updated_source = updated_source;
        this.updated_date = updated_date;
        this.updated_warehouse = updated_warehouse;
        this.created_ip = created_ip;
        this.created_employee = created_employee;
        this.created_client = created_client;
        this.created_source = created_source;
        this.created_date = created_date;
        this.status = status;
        this.pick_warehouse_id = pick_warehouse_id;
        this.deliver_warehouse_id = deliver_warehouse_id;
        this.current_warehouse_id = current_warehouse_id;
        this.return_warehouse_id = return_warehouse_id;
        this.next_warehouse_id = next_warehouse_id;
        this.leadtime = leadtime;
        this.order_date = order_date;
//        this.data = data;
        this.soc_id = soc_id;
        this.s2r_time = s2r_time;
        this.return_time = return_time;
        this.finish_date = finish_date;
//        this.tag = tag;
//        this.is_partial_return = is_partial_return;
//        this.partial_return = partial_return;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getReturn_name() {
        return return_name;
    }

    public void setReturn_name(String return_name) {
        this.return_name = return_name;
    }

    public long getReturn_phone() {
        return return_phone;
    }

    public void setReturn_phone(long return_phone) {
        this.return_phone = return_phone;
    }

    public String getReturn_address() {
        return return_address;
    }

    public void setReturn_address(String return_address) {
        this.return_address = return_address;
    }

    public String getReturn_ward_code() {
        return return_ward_code;
    }

    public void setReturn_ward_code(String return_ward_code) {
        this.return_ward_code = return_ward_code;
    }

    public int getReturn_district_id() {
        return return_district_id;
    }

    public void setReturn_district_id(int return_district_id) {
        this.return_district_id = return_district_id;
    }

    public GHNLocation getReturn_location() {
        return return_location;
    }

    public void setReturn_location(GHNLocation return_location) {
        this.return_location = return_location;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public long getFrom_phone() {
        return from_phone;
    }

    public void setFrom_phone(long from_phone) {
        this.from_phone = from_phone;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getFrom_ward_code() {
        return from_ward_code;
    }

    public void setFrom_ward_code(String from_ward_code) {
        this.from_ward_code = from_ward_code;
    }

    public int getFrom_district_id() {
        return from_district_id;
    }

    public void setFrom_district_id(int from_district_id) {
        this.from_district_id = from_district_id;
    }

    public GHNLocation getFrom_location() {
        return from_location;
    }

    public void setFrom_location(GHNLocation from_location) {
        this.from_location = from_location;
    }

    public int getDeliver_station_id() {
        return deliver_station_id;
    }

    public void setDeliver_station_id(int deliver_station_id) {
        this.deliver_station_id = deliver_station_id;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public long getTo_phone() {
        return to_phone;
    }

    public void setTo_phone(long to_phone) {
        this.to_phone = to_phone;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getTo_ward_code() {
        return to_ward_code;
    }

    public void setTo_ward_code(String to_ward_code) {
        this.to_ward_code = to_ward_code;
    }

    public int getTo_district_id() {
        return to_district_id;
    }

    public void setTo_district_id(int to_district_id) {
        this.to_district_id = to_district_id;
    }

    public GHNLocation getTo_location() {
        return to_location;
    }

    public void setTo_location(GHNLocation to_location) {
        this.to_location = to_location;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getConverted_weight() {
        return converted_weight;
    }

    public void setConverted_weight(int converted_weight) {
        this.converted_weight = converted_weight;
    }

    public String getImage_ids() {
        return image_ids;
    }

    public void setImage_ids(String image_ids) {
        this.image_ids = image_ids;
    }

    public int getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(int service_type_id) {
        this.service_type_id = service_type_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getPayment_type_id() {
        return payment_type_id;
    }

    public void setPayment_type_id(int payment_type_id) {
        this.payment_type_id = payment_type_id;
    }

    public ArrayList<Integer> getPayment_type_ids() {
        return payment_type_ids;
    }

    public void setPayment_type_ids(ArrayList<Integer> payment_type_ids) {
        this.payment_type_ids = payment_type_ids;
    }

    public int getCustom_service_fee() {
        return custom_service_fee;
    }

    public void setCustom_service_fee(int custom_service_fee) {
        this.custom_service_fee = custom_service_fee;
    }

    public String getSort_code() {
        return sort_code;
    }

    public void setSort_code(String sort_code) {
        this.sort_code = sort_code;
    }

    public int getCod_amount() {
        return cod_amount;
    }

    public void setCod_amount(int cod_amount) {
        this.cod_amount = cod_amount;
    }

    public String getCod_collect_date() {
        return cod_collect_date;
    }

    public void setCod_collect_date(String cod_collect_date) {
        this.cod_collect_date = cod_collect_date;
    }

    public String getCod_transfer_date() {
        return cod_transfer_date;
    }

    public void setCod_transfer_date(String cod_transfer_date) {
        this.cod_transfer_date = cod_transfer_date;
    }

    public boolean isIs_cod_transferred() {
        return is_cod_transferred;
    }

    public void setIs_cod_transferred(boolean is_cod_transferred) {
        this.is_cod_transferred = is_cod_transferred;
    }

    public boolean isIs_cod_collected() {
        return is_cod_collected;
    }

    public void setIs_cod_collected(boolean is_cod_collected) {
        this.is_cod_collected = is_cod_collected;
    }

    public int getInsurance_value() {
        return insurance_value;
    }

    public void setInsurance_value(int insurance_value) {
        this.insurance_value = insurance_value;
    }

    public int getOrder_value() {
        return order_value;
    }

    public void setOrder_value(int order_value) {
        this.order_value = order_value;
    }

    public int getPick_station_id() {
        return pick_station_id;
    }

    public void setPick_station_id(int pick_station_id) {
        this.pick_station_id = pick_station_id;
    }

    public String getClient_order_code() {
        return client_order_code;
    }

    public void setClient_order_code(String client_order_code) {
        this.client_order_code = client_order_code;
    }

    public int getCod_failed_amount() {
        return cod_failed_amount;
    }

    public void setCod_failed_amount(int cod_failed_amount) {
        this.cod_failed_amount = cod_failed_amount;
    }

    public String getCod_failed_collect_date() {
        return cod_failed_collect_date;
    }

    public void setCod_failed_collect_date(String cod_failed_collect_date) {
        this.cod_failed_collect_date = cod_failed_collect_date;
    }

    public String getRequired_note() {
        return required_note;
    }

    public void setRequired_note(String required_note) {
        this.required_note = required_note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmployee_note() {
        return employee_note;
    }

    public void setEmployee_note(String employee_note) {
        this.employee_note = employee_note;
    }

    public String getSeal_code() {
        return seal_code;
    }

    public void setSeal_code(String seal_code) {
        this.seal_code = seal_code;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public ArrayList<GHNItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<GHNItem> items) {
        this.items = items;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getVersion_no() {
        return version_no;
    }

    public void setVersion_no(String version_no) {
        this.version_no = version_no;
    }

    public String getUpdated_ip() {
        return updated_ip;
    }

    public void setUpdated_ip(String updated_ip) {
        this.updated_ip = updated_ip;
    }

    public int getUpdated_employee() {
        return updated_employee;
    }

    public void setUpdated_employee(int updated_employee) {
        this.updated_employee = updated_employee;
    }

    public int getUpdated_client() {
        return updated_client;
    }

    public void setUpdated_client(int updated_client) {
        this.updated_client = updated_client;
    }

    public String getUpdated_source() {
        return updated_source;
    }

    public void setUpdated_source(String updated_source) {
        this.updated_source = updated_source;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public int getUpdated_warehouse() {
        return updated_warehouse;
    }

    public void setUpdated_warehouse(int updated_warehouse) {
        this.updated_warehouse = updated_warehouse;
    }

    public String getCreated_ip() {
        return created_ip;
    }

    public void setCreated_ip(String created_ip) {
        this.created_ip = created_ip;
    }

    public int getCreated_employee() {
        return created_employee;
    }

    public void setCreated_employee(int created_employee) {
        this.created_employee = created_employee;
    }

    public int getCreated_client() {
        return created_client;
    }

    public void setCreated_client(int created_client) {
        this.created_client = created_client;
    }

    public String getCreated_source() {
        return created_source;
    }

    public void setCreated_source(String created_source) {
        this.created_source = created_source;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPick_warehouse_id() {
        return pick_warehouse_id;
    }

    public void setPick_warehouse_id(int pick_warehouse_id) {
        this.pick_warehouse_id = pick_warehouse_id;
    }

    public int getDeliver_warehouse_id() {
        return deliver_warehouse_id;
    }

    public void setDeliver_warehouse_id(int deliver_warehouse_id) {
        this.deliver_warehouse_id = deliver_warehouse_id;
    }

    public int getCurrent_warehouse_id() {
        return current_warehouse_id;
    }

    public void setCurrent_warehouse_id(int current_warehouse_id) {
        this.current_warehouse_id = current_warehouse_id;
    }

    public int getReturn_warehouse_id() {
        return return_warehouse_id;
    }

    public void setReturn_warehouse_id(int return_warehouse_id) {
        this.return_warehouse_id = return_warehouse_id;
    }

    public int getNext_warehouse_id() {
        return next_warehouse_id;
    }

    public void setNext_warehouse_id(int next_warehouse_id) {
        this.next_warehouse_id = next_warehouse_id;
    }

    public String getLeadtime() {
        return leadtime;
    }

    public void setLeadtime(String leadtime) {
        this.leadtime = leadtime;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }

    public String getSoc_id() {
        return soc_id;
    }

    public void setSoc_id(String soc_id) {
        this.soc_id = soc_id;
    }

    public String getS2r_time() {
        return s2r_time;
    }

    public void setS2r_time(String s2r_time) {
        this.s2r_time = s2r_time;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

//    public ArrayList<String> getTag() {
//        return tag;
//    }
//
//    public void setTag(ArrayList<String> tag) {
//        this.tag = tag;
//    }
//
//    public boolean isIs_partial_return() {
//        return is_partial_return;
//    }
//
//    public void setIs_partial_return(boolean is_partial_return) {
//        this.is_partial_return = is_partial_return;
//    }
//
//    public ArrayList<String> getPartial_return() {
//        return partial_return;
//    }
//
//    public void setPartial_return(ArrayList<String> partial_return) {
//        this.partial_return = partial_return;
//    }
}
