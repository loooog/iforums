/*
 * Created on 28/08/2006 23:12:09
 */
package net.iforums.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.iforums.JForumExecutionContext;
import net.iforums.beans.MailIntegration;
import net.iforums.dao.BaseORMDao;
import net.iforums.dao.MailIntegrationDao;
import net.iforums.utils.DbUtils;
import net.iforums.utils.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: GenericMailIntegrationDao.java,v 1.3 2006/10/10 00:19:09 rafaelsteil Exp $
 */
@Repository
public class MailIntegrationDaoImpl extends BaseORMDao<MailIntegration> implements MailIntegrationDao
{
	/**
	 * @see net.iforums.dao.MailIntegrationDao#add(net.jforum.entities.MailIntegration)
	 */
	public void add(MailIntegration integration)
	{
		PreparedStatement p = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("MailIntegration.add"));
			this.prepareForSave(integration, p);
			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	/**
	 * @see net.iforums.dao.MailIntegrationDao#delete(int)
	 */
	public void delete(int forumId)
	{
		PreparedStatement p = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("MailIntegration.delete"));
			p.setInt(1, forumId);
			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see net.iforums.dao.MailIntegrationDao#find(int)
	 */
	public MailIntegration find(int forumId)
	{
		MailIntegration m = null;
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MailIntegration.find"));
			p.setInt(1, forumId);
			rs = p.executeQuery();
			
			if (rs.next()) {
				m = this.buildMailIntegration(rs);
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return m;
	}

	/**
	 * @see net.iforums.dao.MailIntegrationDao#findAll()
	 */
	public List findAll()
	{
		List l = new ArrayList();
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MailIntegration.findAll"));
			rs = p.executeQuery();
			
			while (rs.next()) {
				l.add(this.buildMailIntegration(rs));
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return l;
	}

	/**
	 * @see net.iforums.dao.MailIntegrationDao#update(net.jforum.entities.MailIntegration)
	 */
	public void update(MailIntegration integration)
	{
		PreparedStatement p = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("MailIntegration.update"));
			
			this.prepareForSave(integration, p);
			p.setInt(8, integration.getForumId());
			
			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	private MailIntegration buildMailIntegration(ResultSet rs) throws SQLException
	{
		MailIntegration mi = new MailIntegration();
		
		mi.setForumId(rs.getInt("forum_id"));
		mi.setForumEmail(rs.getString("forum_email"));
		mi.setPopHost(rs.getString("pop_host"));
		mi.setPopPassword(rs.getString("pop_password"));
		mi.setPopPort(rs.getInt("pop_port"));
		mi.setPopUsername(rs.getString("pop_username"));
		mi.setSSL(rs.getInt("pop_ssl") == 1);
		
		return mi;
	}

	/**
	 * Given a PreparedStatement, fill its values with the data of a MailIntegration instance
	 * @param integration the data to fill the statement
	 * @param p the statement to be filled
	 * @throws SQLException
	 */
	private void prepareForSave(MailIntegration integration, PreparedStatement p) throws SQLException
	{
		p.setInt(1, integration.getForumId());
		p.setString(2, integration.getForumEmail());
		p.setString(3, integration.getPopHost());
		p.setString(4, integration.getPopUsername());
		p.setString(5, integration.getPopPassword());
		p.setInt(6, integration.getPopPort());
		p.setInt(7, integration.isSSL() ? 1 : 0);
	}
}