package com.doudou.dispatch.trip.commons;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.entities.OperationLog;
import com.dispatch.gps.commons.utils.OperationThreadLocal;

public class HttpRespResult<T> {
    private int code;			// 返回码
    private String define;		// 返回信息
    private T result;		// 返回数据

    public HttpRespResult(){
        this.define = HttpApiConstans.err_define;
        this.code = HttpApiConstans.error;
    }

    public HttpRespResult(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public HttpRespResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getDefine() {
        return define;
    }

    public HttpRespResult setDefine(String define) {
        this.define = define;
        OperationLog log = OperationThreadLocal.get();
        if(null != log) {
            log.setDefine(define);
        }
        return this;
    }

    public T getResult() {
        return result;
    }

    public HttpRespResult setResult(T result) {
        this.result = result;
        return this;
    }
}
