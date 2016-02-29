package com.d2c;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.d2c.util.GsonMessageBodyHandler;
import com.d2c.web.resource.AssignmentResource;
import com.d2c.web.resource.CodeResource;
import com.d2c.web.resource.CourseResource;
import com.d2c.web.resource.FileResource;
import com.d2c.web.resource.GradeResource;
import com.d2c.web.resource.MainPageResource;
import com.d2c.web.resource.ProfessorResource;
import com.d2c.web.resource.StudentResource;
import com.d2c.web.resource.SubmissionResource;
import com.d2c.web.resource.TeachingAssistantResource;

public class D2CApplication extends Application {
	@Override
	public Set<Class<?>> getClasses(){
		Set<Class<?>> s = new HashSet<>();
		s.add(AssignmentResource.class);
		s.add(CodeResource.class);
		s.add(CourseResource.class);
		s.add(FileResource.class);
		s.add(GradeResource.class);
		s.add(MainPageResource.class);
		s.add(ProfessorResource.class);
		s.add(StudentResource.class);
		s.add(SubmissionResource.class);
		s.add(TeachingAssistantResource.class);
		s.add(GsonMessageBodyHandler.class);
		return s;
	}
}
