using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SnapTown.Models
{
    public class Subscription
    {
        public int SubscriptionID { get; set; }
        public int UserID { get; set; }
        public int TownID { get; set; }

        public virtual User User { get; set; }
        public virtual Town Town { get; set; }
    }
}
