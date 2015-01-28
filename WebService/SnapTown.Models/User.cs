using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SnapTown.Models
{
    public class User
    {
        public int UserID { get; set; }
        public string Name { get; set; }
        public string AuthToken { get; set; }
        public string GCMClientToken { get; set; }
    }
}
