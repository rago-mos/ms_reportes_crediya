package co.com.crediya.model.utils;

public class Constant {

    public static final String TABLE_NAME = "loan_report";
    public static final String ID_TABLE_VALUE = "id_loan_report_001";
    public static final String TOTAL_LOAN = "total_loan";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String ID_TABLE = "id_loan_report";
    public static final String SET_UPDATED_SENTENCE = "SET total_loan = if_not_exists(total_loan, :zero) + :addLoan, total_amount = if_not_exists(total_amount, :zero) + :addAmount";

    public static final String LOG_INFO_REPORT = "report generated";
    public static final String LOG_INFO_GET_REPORT = "Successfully updating report";
    public static final String LOG_INFO_SQS_RECEIVED_REPORT = "Message sqs report received: {}";

    public static final String MESSAGE_REPORT_SUCCEFULLY = "Report loan created successfully";

    public static final String STATUS_OK = "CODE_001";
    public static final String STATUS_ERROR = "CODE_003";

    public static final String ERROR_BAD_TOKEN = "bad token";
    public static final String ERROR_JSON_PROCESSING = "Error serializing message";
    public static final String ERROR_JSON_PROCESSING_DES = "Error deserializing message";
    public static final String ERROR_LOAN_TYPE = "loan type not found";
    public static final String ERROR_ACCES_DENIED = "Access denied. You do not have the necessary permissions for this resource";
    public static final String ERROR_ACCES_DENIED_ARGUMENT = "Access denied. You do not have the necessary permissions for this resource: {}";
    public static final String ERROR_DYNAMO_GET = "An error occurred while querying the database";
    public static final String ERROR_DYNAMO_UPDATED = "An error occurred while updating the database: {}";


    private Constant(){
        throw new UnsupportedOperationException("util class");
    }
}
