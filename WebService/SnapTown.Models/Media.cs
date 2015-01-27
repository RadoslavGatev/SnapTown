using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SnapTown.Models
{
    public class Media
    {
        public int MediaID { get; set; }
        public string Path { get; set; }
        public string Description { get; set; }
        public MediaType Type { get; set; }

        public DateTime UploadedOn { get; set; }

        public int OwnerID { get; set; }
        public virtual User Owner { get; set; }
    }
}
