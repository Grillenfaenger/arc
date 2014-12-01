package de.spinfo.arc.annotationmodel.annotatable;
import java.util.List;
import java.util.Map;

import de.spinfo.arc.annotationmodel.annotation.Annotation;
import de.spinfo.arc.annotationmodel.annotation.Annotation.AnnotationTypes;
//@Document(collection = "words")
public interface HasAnnotations  {

	public Map<AnnotationTypes, List<Annotation>> getAnnotations();
	public List<Annotation> getAnnotationsOfType(AnnotationTypes type);
	
	public void setAnnotationAsType(AnnotationTypes type, Annotation annotation);
	public void setAnnotationsAsType(AnnotationTypes type, List<Annotation> annotations);
	boolean removeAnnotation(AnnotationTypes annotationTypes,
			Annotation annotation);
	
	
}
