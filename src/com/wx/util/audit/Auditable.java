/*
 * Copyright (C) 2012 Raffaele Canale - raffaelecanale@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wx.util.audit;

/**
 * WXLibraries
 * Auditable.java (UTF-8)
 * 
 * TODO Write doc
 *
 * Date: 10 oct. 2012
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 */
public interface Auditable {

    default AuditLogs newAudit(int maxDepth) {
        AuditLogs logs = new AuditLogs();
        auditErrors(maxDepth, logs);
        return logs;
    }

    void auditErrors(int depth, AuditLogs message);
}
