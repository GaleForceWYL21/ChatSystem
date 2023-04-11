package com.wyl.gpt.Utils;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
        put("result", new HashMap<>());
    }


    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(int code,Object data) {
        R r = new R();
        r.put("code", code);
        r.put("msg", "操作失败");
        r.put("result", data);
        return r;
    }

    public static R ok(int code,Object data) {
        R r = new R();
        r.put("code", code);
        r.put("msg", "操作成功");
        r.put("result", data);
        return r;
    }

    public static R error(Object data) {
        R r = new R();
        r.put("msg", "操作失败");
        r.put("result", data);
        return r;
    }


    public static R ok(String msg, Object data) {
        R r = new R();
        r.put("msg", msg);
        r.put("result", data);
        return r;
    }




    public static R ok(Object data) {
        R r = new R();
        r.put("result", data);
        return r;
    }

    public static R ok() {
        return new R();
    }


    public static R status(boolean flag) {
        return flag ? R.ok() : R.error("操作失败");
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public R add(String key, Object value) {
        Map map = (Map) super.get("result");
        map.put(key, value);
        return this;
    }
}
