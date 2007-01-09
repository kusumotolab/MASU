package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Member インターフェースを利用した処理を行うクラス．
 * 
 * @author y-higo
 *
 */
public class Members {

    /**
     * 引数で与えられたメンバーのうち，スタティックなものだけを抽出して返す．
     * 
     * @param members メンバーの List
     * @return スタティックなメンバーの List
     */
    public static final List getInstanceMembers(final List<? extends Member> members){
        
        final List<Member> instanceMembers = new ArrayList<Member>();
        for (Member member : members){
            if (member.isInstanceMember()){
                instanceMembers.add(member);
            }
        }
        
        return Collections.unmodifiableList(instanceMembers);
    }
    
    /**
     * 引数で与えられたメンバーのうち，インスタンスなものだけを抽出して返す．
     * 
     * @param members メンバーの List
     * @return インスタンスメンバーの List
     */
    public static final List getStaticMembers(final List<? extends Member> members){
        
        final List<Member> staticMembers = new ArrayList<Member>();
        for (Member member : members){
            if (member.isStaticMember()){
                staticMembers.add(member);
            }
        }
        
        return Collections.unmodifiableList(staticMembers);
    }
    
    /**
     * 引数で与えられたメンバーのうち，スタティックなものだけを抽出して返す．
     * 
     * @param members メンバーの SortedSet
     * @return スタティックなメンバーの SortedSet
     */
    public static final SortedSet getInstanceMembers(final SortedSet<? extends Member> members){
        
        final SortedSet<Member> instanceMembers = new TreeSet<Member>();
        for (Member member : members){
            if (member.isInstanceMember()){
                instanceMembers.add(member);
            }
        }
        
        return Collections.unmodifiableSortedSet(instanceMembers);
    }
    
    /**
     * 引数で与えられたメンバーのうち，インスタンスなものだけを抽出して返す．
     * 
     * @param members メンバーの SortedSet
     * @return インスタンスメンバーの SortedSet
     */
    public static final SortedSet getStaticMembers(final SortedSet<? extends Member> members){
        
        final SortedSet<Member> staticMembers = new TreeSet<Member>();
        for (Member member : members){
            if (member.isStaticMember()){
                staticMembers.add(member);
            }
        }
        
        return Collections.unmodifiableSortedSet(staticMembers);
    }
}
