using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace SnapTown.DTO
{
    public class MediaDto
    {
        public int MediaId { get; set; }
        public string Description { get; set; }
        public byte Type { get; set; }
        public DateTime UploadedOn { get; set; }
        public string UploadedBy { get; set; }
    }
}
