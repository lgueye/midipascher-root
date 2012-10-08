package fr.midipascher.domain;

import org.joda.time.DateTime;

/**
 * User: lgueye Date: 08/10/12 Time: 18:22
 */
public interface EventAware {
    void setCreated(DateTime created);
    void setUpdated(DateTime updated);
}
