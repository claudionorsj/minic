	.text
	.globl main
main:
	subq $16, %rsp
	movq $16, %rdi
	call sbrk
	movq %rax, 8(%rsp)
	movq $24, %rdi
	call sbrk
	movq %rax, 0(%rsp)
	movq $16, %rdi
	call sbrk
	movq 0(%rsp), %rdi
	movq %rax, 8(%rdi)
	movq $65, %rcx
	movq 0(%rsp), %rdi
	movq %rcx, 0(%rdi)
	movq $66, %rcx
	movq 0(%rsp), %rdi
	movq %rcx, 16(%rdi)
	movq $120, %rcx
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rcx, 0(%rdi)
	movq $121, %rcx
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq %rcx, 8(%rdi)
	movq 0(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 16(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $88, %rcx
	movq 8(%rsp), %rdi
	movq %rcx, 0(%rdi)
	movq $89, %rcx
	movq 8(%rsp), %rdi
	movq %rcx, 8(%rdi)
	movq 0(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 16(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq 8(%rsp), %rcx
	movq 0(%rsp), %rdi
	movq %rcx, 8(%rdi)
	movq 0(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rdi
	movq 16(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $16, %rsp
	ret
	.data
