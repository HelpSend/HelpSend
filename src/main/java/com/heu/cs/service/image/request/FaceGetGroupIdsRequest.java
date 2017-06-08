/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heu.cs.service.image.request;

import com.heu.cs.service.image.exception.ParamException;

/**
 *
 * @author serenazhao 获取组列表
 */
public class FaceGetGroupIdsRequest extends AbstractBaseRequest {
        
        public FaceGetGroupIdsRequest(String bucketName) {
		super(bucketName);

	}

	@Override
	public void check_param() throws ParamException {
		super.check_param();
	}
}
