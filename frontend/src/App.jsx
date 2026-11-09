import {useEffect,useState} from 'react';
const API='http://localhost:8080/api';
async function request(path,method='GET',body,token){
 const response=await fetch(API+path,{method,headers:{'Content-Type':'application/json',...(token?{Authorization:'Bearer '+token}:{})},...(body?{body:JSON.stringify(body)}:{})});
 const data=await response.json().catch(()=>({}));
 if(!response.ok)throw new Error(data.message||'Request failed');
 return data;
}
export default function App(){
 const [user,setUser]=useState(()=>JSON.parse(sessionStorage.getItem('leaveUser')||'null'));
 const [register,setRegister]=useState(false);
 const [auth,setAuth]=useState({name:'',email:'',password:''});
 const [leave,setLeave]=useState({startDate:'',endDate:'',reason:''});
 const [items,setItems]=useState([]);
 const [message,setMessage]=useState('');
 const [search,setSearch]=useState('');
 const admin=user?.role==='ADMIN';
 async function refresh(){
  if(!user)return;
  try{setItems(await request(admin?'/admin/leaves':'/leaves/my','GET',null,user.token));}
  catch(e){setMessage(e.message);}
 }
 useEffect(()=>{refresh();},[user?.token]);
 async function authenticate(e){
  e.preventDefault();setMessage('');
  try{
   if(register){await request('/auth/register','POST',auth);setRegister(false);setMessage('Registered. Please sign in.');}
   else{const result=await request('/auth/login','POST',auth);setUser(result);sessionStorage.setItem('leaveUser',JSON.stringify(result));}
  }catch(e){setMessage(e.message);}
 }
 async function submit(e){
  e.preventDefault();
  try{await request('/leaves','POST',leave,user.token);setLeave({startDate:'',endDate:'',reason:''});setMessage('Leave submitted');refresh();}
  catch(e){setMessage(e.message);}
 }
 async function decide(id,action){
  try{await request('/admin/leaves/'+id+'/'+action,'PUT',null,user.token);setMessage('Request '+(action==='approve'?'approved':'rejected'));refresh();}
  catch(e){setMessage(e.message);}
 }
 if(!user)return <main className="auth"><section className="login card"><h1>LeaveDesk</h1><p>Employee Leave Management</p><h2>{register?'Create account':'Sign in'}</h2><form onSubmit={authenticate}>
 {register&&<label>Name<input required value={auth.name} onChange={e=>setAuth({...auth,name:e.target.value})}/></label>}
 <label>Email<input required type="email" value={auth.email} onChange={e=>setAuth({...auth,email:e.target.value})}/></label>
 <label>Password<input required type="password" minLength="8" value={auth.password} onChange={e=>setAuth({...auth,password:e.target.value})}/></label>
 <button>{register?'Register':'Log in'}</button></form><button className="link" onClick={()=>setRegister(!register)}>{register?'Already registered? Sign in':'New employee? Register'}</button><p className="message">{message}</p></section></main>;
 const shown=items.filter(x=>[x.employeeName,x.reason,x.status].join(' ').toLowerCase().includes(search.toLowerCase()));
 return <div className="shell"><aside><h2>▣ LeaveDesk</h2><nav>Dashboard</nav><div className="profile"><b>{user.name}</b><p>{user.role}</p><button onClick={()=>{sessionStorage.removeItem('leaveUser');setUser(null);}}>Sign out</button></div></aside><main className="content"><header><span>Workspace / Overview</span><b>{admin?'Administrator':'Employee'} portal</b></header><div className="body"><h1>{admin?'Leave approvals':'Welcome, '+user.name}</h1><p className="sub">{admin?'Manage employee leave requests':'Apply for leave and track your requests'}</p><div className="stats">{[['Total',items.length],['Pending',items.filter(x=>x.status==='PENDING').length],['Approved',items.filter(x=>x.status==='APPROVED').length],['Rejected',items.filter(x=>x.status==='REJECTED').length]].map(([label,value])=><div className="card stat" key={label}><p>{label}</p><strong>{value}</strong></div>)}</div>
 {message&&<p className="notice">{message}</p>}
 {!admin&&<section className="card formcard"><h2>Apply for leave</h2><form onSubmit={submit}><div className="dates"><label>Start date<input type="date" required min={new Date().toLocaleDateString('en-CA')} value={leave.startDate} onChange={e=>setLeave({...leave,startDate:e.target.value})}/></label><label>End date<input type="date" required min={leave.startDate||new Date().toLocaleDateString('en-CA')} value={leave.endDate} onChange={e=>setLeave({...leave,endDate:e.target.value})}/></label></div><label>Reason<textarea required maxLength="500" value={leave.reason} onChange={e=>setLeave({...leave,reason:e.target.value})}/></label><button>Submit leave request</button></form></section>}
 <section className="card list"><div className="listhead"><h2>{admin?'All requests':'My leave requests'}</h2><input placeholder="Search requests" value={search} onChange={e=>setSearch(e.target.value)}/></div><div className="scroll"><table><thead><tr>{admin&&<th>Employee</th>}<th>Dates</th><th>Reason</th><th>Status</th>{admin&&<th>Action</th>}</tr></thead><tbody>{shown.map(x=><tr key={x.id}>{admin&&<td><b>{x.employeeName}</b><small>{x.employeeEmail}</small></td>}<td>{x.startDate} to {x.endDate}</td><td>{x.reason}</td><td><span className={'badge '+x.status.toLowerCase()}>{x.status}</span></td>{admin&&<td>{x.status==='PENDING'?<div className="buttons"><button onClick={()=>decide(x.id,'approve')}>Approve</button><button className="danger" onClick={()=>decide(x.id,'reject')}>Reject</button></div>:'Processed'}</td>}</tr>)}</tbody></table>{!shown.length&&<p className="empty">No leave requests found.</p>}</div></section></div></main></div>;
}