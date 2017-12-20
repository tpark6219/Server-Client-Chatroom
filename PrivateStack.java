public class PrivateStack {
  
	private int maxSize;
   public SessionThread[] stackArray;
   private int count;
   public int users;
   public PrivateStack(int s) {
      maxSize = s;
      stackArray = new SessionThread[maxSize];
      count = -1;
      users = -1;
   }
   public void push(SessionThread j) {
	   users++;
      stackArray[users] = j;
      
   }

   public boolean findIfActive(String username)
   {  
	 
	 for (int i = 0; i <  stackArray.length; i++)
	 {	
		 if(stackArray[i] == null)
		 return false;
         if (stackArray[i].username.equals(username))
            return true;
	 }
      
	 	return false;
   }

   public int remove(String j)
   { int pos = -1;
	   for(int i = 0; i < stackArray.length; i++)
	   {	if(stackArray[i] == null)
	   {
		   continue;
	   }
	   else if(stackArray[i].username.equals(j))
		   {
			   stackArray[i] = null;
			   pos = i;
			   break;
		   }
	   }
       if (pos < users && pos != -1)
           for (int i = pos+1; i < users+1; i++)
              stackArray[i-1] = stackArray[i];
        users--;
        
        return pos;
   }
   
   public boolean isEmpty() {
      return (users == -1);
   }
   public boolean isFull() {
      return (users == maxSize - 1);
   }
}