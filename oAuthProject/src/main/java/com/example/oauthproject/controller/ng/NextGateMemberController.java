package com.example.oauthproject.controller.ng;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauthproject.model.ng.MemberInfo;
import com.example.oauthproject.model.ng.UpdateInfo;
import com.example.oauthproject.service.nextgate.NextGateMemberCreateService;
import com.example.oauthproject.service.nextgate.NextGateMemberSearchService;

/**
 * @author ***** 
 * @version 1.0 17 Oct 2016
 * Next Gate Member Controller Class
 */
@RestController
public class NextGateMemberController {
    @Autowired
    NextGateMemberSearchService memberSearchService;
    @RequestMapping(value="/ngMemberSearch", method = RequestMethod.POST)
    public List<MemberInfo> getMemberSearch(@RequestBody MemberInfo memberInfo) throws JSONException {
     //   LoggerUtil.logInfo("Inside NextGate Member Search Controller");
        List<MemberInfo> memberList = memberSearchService.ngSearchMember(memberInfo);
        return memberList;
    }
    @Autowired
    NextGateMemberCreateService memberCreateService;
    @RequestMapping(value="/ngMemberCreate", method = RequestMethod.POST)
    public List<MemberInfo> getMemberInfo(@RequestBody MemberInfo memberInfo) throws JSONException {
      //  LoggerUtil.logInfo("Inside NextGate Member Create Controller");
        List<MemberInfo> membersList = memberCreateService.ngCreateMember(memberInfo);
        return membersList;
    }
        }
