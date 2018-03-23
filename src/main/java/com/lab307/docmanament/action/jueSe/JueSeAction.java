/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lab307.docmanament.action.jueSe;

/**
 *
 * @author Administrator
 */
import com.dengfeng.std.DengFengAction;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;

@Controller
public class JueSeAction extends DengFengAction {

    @Resource(name = "JueSe")
    JueSe js;

    public String jueSe() {
        return this.feiFa ? "login" : "success";
    }

    public String cxJueSe() {
        if (this.feiFa) {
            return reLogin();
        }
        JSONObject result  = this.js.cxJueSe(qybh);
        renderJSON(result);
        return null;
    }

}
