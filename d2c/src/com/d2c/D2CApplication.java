package com.d2c;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.d2c.util.GsonMessageBodyHandler;
import com.d2c.web.resources.AssignmentResource;
import com.d2c.web.resources.CodeResource;
import com.d2c.web.resources.CourseInstanceResource;
import com.d2c.web.resources.FileResource;
import com.d2c.web.resources.GradeResource;
import com.d2c.web.resources.MainPageResource;
import com.d2c.web.resources.AccountResource;
import com.d2c.web.resources.SubmissionResource;

public class D2CApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<>();
		s.add(AssignmentResource.class);
		s.add(CodeResource.class);
		s.add(CourseInstanceResource.class);
		s.add(FileResource.class);
		s.add(GradeResource.class);
		s.add(MainPageResource.class);
		s.add(AccountResource.class);
		s.add(SubmissionResource.class);
		s.add(GsonMessageBodyHandler.class);
		return s;
	}
}
