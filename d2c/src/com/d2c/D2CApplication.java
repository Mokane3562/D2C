package com.d2c;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class D2CApplication extends Application {
	@Override
	public Set<Class<?>> getClasses(){
		Set<Class<?>> s = new HashSet<>();
		s.add(AssignmentResource.class);
		s.add(CodeHandler.class);
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
