package com.kiddyMarket.Logic.Constants;

public final class APIConstants {

    private static final String BANKSERVER = "http://localhost:8888/";
    private static final String INVENTORYSERVER = "http://localhost:8881/";

    public static final String AUTH_ACCOUNTS = BANKSERVER + "accounts/";
    public static final String AUTHHEADER = "Authorization";

    public static final String BANKCALL = BANKSERVER + "bank/";
    public static final String TRANSFER_MONEY = BANKSERVER + "bank/transfer";
    public static final String GET_BANK_ACCOUNTS = BANKSERVER + "bank/all/";

    public static final String TRANSFER_ITEM = INVENTORYSERVER + "inventory/transfer";
    public static final String INVENTORY_ACCOUNTS = INVENTORYSERVER + "inventory/account/";
    public static final String INVENTORY_ITEM_GET = INVENTORYSERVER + "item/";
}