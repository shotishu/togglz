package org.togglz.core.metadata;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.group.AnnotationFeatureGroup;
import org.togglz.core.group.FeatureGroup;
import org.togglz.core.util.FeatureAnnotations;

/**
 * 
 * Implementation of {@link FeatureMetaData} that looks for annotations like {@link Label} and {@link EnabledByDefault} on
 * feature enums.
 * 
 * @author Christian Kaltepoth
 * 
 */
public class EnumFeatureMetaData implements FeatureMetaData {

    private final String label;

    private final boolean enabledByDefault;

    private final Set<FeatureGroup> groups = new HashSet<FeatureGroup>();

    private final Map<String, String> attributes = new LinkedHashMap<String, String>();

    public EnumFeatureMetaData(Feature feature) {

        // lookup label via @Label annotation
        this.label = FeatureAnnotations.getLabel(feature);

        // lookup default via @EnabledByDefault
        this.enabledByDefault = FeatureAnnotations.isEnabledByDefault(feature);

        // process annotations on the feature
        for (Annotation annotation : FeatureAnnotations.getAnnotations(feature)) {

            // lookup groups
            FeatureGroup group = AnnotationFeatureGroup.build(annotation.annotationType());
            if (group != null) {
                groups.add(group);
            }

            // check if this annotation is a feature attribute
            String[] attribute = FeatureAnnotations.getFeatureAttribute(annotation);
            if (attribute != null) {
                attributes.put(attribute[0], attribute[1]);
            }

        }

    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    @Override
    public Set<FeatureGroup> getGroups() {
        return groups;
    }

    @Override
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

}
